from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
import logging
import numpy as np
import os

logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app)

# -----------------------------
# Model registry: modelType -> filename
# -----------------------------
MODEL_FILES = {
    "svm": "svm_complete_model.pkl",
    "xgboost": "xgboost_complete_model.pkl",
    "random_forest": "random_forest_complete_model.pkl",
    "mlp": "mlp_complete_model.pkl",
    "decision_tree": "decision_tree_complete_model.pkl",
}

# Cache loaded models so we don't reload on every request
MODEL_CACHE = {}

# -----------------------------
# Mapping: Chinese -> English canonical feature names
# -----------------------------
cn_to_eng = {
    '性别': 'Sex',
    '年龄': 'Age',
    '癸酸': 'Decanoic Acid',
    '月桂酸': 'Lauric Acid',
    '肉豆蔻烯酸': 'Myristoleic Acid',
    '肉豆蔻酸': 'Myristic Acid',
    '棕榈烯酸': 'Palmitoleic Acid',
    '棕榈酸': 'Palmitic Acid',
    'α-亚麻酸': 'Linolenic Acid',
    '亚油酸': 'Linoleic Acid',
    '油酸': 'Oleic Acid',
    '硬脂酸': 'Stearic Acid',
    '二十碳五烯酸': 'Eicosapentaenoic Acid',  # EPA
    '二十碳四烯酸': 'Arachidonic Acid',
    '二十碳二烯酸': 'Eicosadienoic Acid',
    '二十碳一烯酸': 'Eicosenoic Acid',
    '二十碳酸': 'Arachidic Acid',
    '二十二碳酸': 'Behenic Acid',
    '二十二碳一烯酸': 'Erucic Acid',
    '二十二碳四烯酸': 'Docosatetraenoic Acid',
    '二十二碳五烯酸': 'Docosapentaenoic Acid',
    '二十二碳六烯酸': 'Docosahexaenoic Acid',
    '二十四碳一烯酸': 'Nervonic Acid',
    '二十四碳酸': 'Lignoceric Acid',
    'ω-3/ω-6 比值': 'w3/w6',
    '三烯/四烯比': 'Triene/Tetraene',
    '总饱和脂肪酸': 'Total SFA',
    '总单不饱和脂肪酸': 'Total MUFA',
    '总多不饱和脂肪酸': 'Total PUFA',
    '总 ω-3': 'T w3',
    '总 ω-6': 'T w6',
    '总脂肪酸': 'Total FA'
}

english_aliases = {
    "Year": "Age",
    "Triene /Tetraene": "Triene/Tetraene",
    "Triene/Tetraene": "Triene/Tetraene",
    "EPA": "Eicosapentaenoic Acid",
    "DHA": "Docosahexaenoic Acid",
    "DPA": "Docosapentaenoic Acid",
}

def to_float(v):
    try:
        return float(v)
    except (TypeError, ValueError):
        return 0.0

def build_processed_features(payload: dict):
    """
    Converts incoming payload into a canonical {EnglishFeatureName: float} dict.
    Accepts:
    - Chinese keys (mapped via cn_to_eng)
    - English keys (kept, with alias normalization)
    """
    processed = {}

    # Chinese -> English
    for cn_key, eng_key in cn_to_eng.items():
        if cn_key in payload:
            processed[eng_key] = to_float(payload.get(cn_key))

    # English keys + normalize
    for k, v in payload.items():
        if k in ("result", "label", "target", "modelType"):
            continue
        canonical = english_aliases.get(k, k)
        processed[canonical] = to_float(v)

    return processed

def unwrap_model_package(raw):
    """
    Your pkl might be a dict like:
      {'model': model, 'scaler': scaler, 'feature_names': [...], ...}
    or it might be directly the sklearn model.
    """
    if isinstance(raw, dict):
        model = raw.get("model") or raw.get("clf") or raw.get("pipeline") or raw
        scaler = raw.get("scaler")
        feature_names = raw.get("feature_names") or raw.get("columns")
        return model, scaler, feature_names
    return raw, None, None

def load_model_by_type(model_type: str):
    model_type = (model_type or "svm").strip().lower()

    # allow friendly aliases
    aliases = {
        "rf": "random_forest",
        "randomforest": "random_forest",
        "random_forest": "random_forest",
        "xgb": "xgboost",
        "xgboost": "xgboost",
        "svm": "svm",
        "mlp": "mlp",
        "decisiontree": "decision_tree",
        "decision_tree": "decision_tree",
        "dt": "decision_tree",
    }
    model_type = aliases.get(model_type, model_type)

    if model_type not in MODEL_FILES:
        raise ValueError(f"Unknown modelType '{model_type}'. Allowed: {list(MODEL_FILES.keys())}")

    if model_type in MODEL_CACHE:
        return MODEL_CACHE[model_type]

    filename = MODEL_FILES[model_type]
    if not os.path.exists(filename):
        raise FileNotFoundError(f"Model file not found: {filename}")

    raw = joblib.load(filename)
    model, scaler, feature_names = unwrap_model_package(raw)

    MODEL_CACHE[model_type] = (model, scaler, feature_names)
    logger.info(f"Loaded {model_type} from {filename} | model={type(model)} | scaler={type(scaler)}")
    if feature_names:
        logger.info(f"{model_type} feature_names count: {len(feature_names)}")
    return MODEL_CACHE[model_type]

def predict_probability(model, X):
    if hasattr(model, "predict_proba"):
        return float(model.predict_proba(X)[0][1])
    if hasattr(model, "decision_function"):
        score = float(model.decision_function(X)[0])
        return float(1.0 / (1.0 + np.exp(-score)))
    pred = int(model.predict(X)[0])
    return float(pred)

@app.route("/api/predict", methods=["POST"])
def predict():
    try:
        payload = request.get_json(force=True) or {}
        model_type = payload.get("modelType", "svm")

        model, scaler, feature_names = load_model_by_type(model_type)

        processed = build_processed_features(payload)

        # If the model package saved feature order, use it. Otherwise fall back to sorted keys.
        if feature_names:
            ordered = list(feature_names)
        else:
            ordered = sorted(processed.keys())
            logger.warning("No saved feature_names in model package; using sorted keys (may reduce accuracy).")

        X = np.array([[processed.get(name, 0.0) for name in ordered]], dtype=float)

        # Apply scaler if present (important for SVM/MLP)
        if scaler is not None:
            X = scaler.transform(X)

        prob = predict_probability(model, X)

        # Basic label (optional, but your UI expects disease/suggestion)
        disease = "diabetes" if prob >= 0.5 else "no diabetes"
        suggestion = (
            "Maintain a healthy lifestyle, monitor blood sugar regularly, and follow medical advice."
            if prob >= 0.5 else
            "Risk appears low. Continue healthy habits and regular check-ups."
        )

        return jsonify({
            "modelType": model_type,
            "probability": prob,
            "percentage": prob * 100.0,
            "disease": disease,
            "suggestion": suggestion
        })

    except Exception as e:
        logger.error(f"Error during prediction: {e}", exc_info=True)
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5000, debug=True)
