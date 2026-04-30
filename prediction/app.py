from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
import logging
import numpy as np
import os

logging.basicConfig(
    level=logging.DEBUG,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler("prediction.log"),
        logging.StreamHandler()
    ]
)
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
    '年龄': 'Year',
    '癸酸': 'Decanoric Acid',
    '月桂酸': 'Laurric Acid',
    '肉豆蔻酸': 'Myristoleic Acid',
    '顺-9-十四碳烯酸': 'Myristic Acid',
    '棕榈酸': 'Palmitoleic Acid',
    '棕榈油酸': 'Palmitic Acid',
    'α-亚麻酸': 'Linolenic Acid',
    '亚油酸': 'Linoleic Acid',
    '油酸': 'Oleic Acid',
    '硬脂酸': 'Stearic Acid',
    '二十碳五烯酸': 'EPA',
    '二十碳四烯酸': 'Arachidonic Acid',
    '二十碳三烯酸': 'Mead Acid',
    '二十碳二烯酸': 'Eicosadienoic Acid',
    '二十碳一烯酸': 'Eicosenoic Acid',
    '二十碳酸': 'Arachidic Acid',
    '二十二碳六烯酸': 'DHA',
    '二十二碳五烯酸': 'DPA',
    '二十二碳四烯酸': 'DTA',
    '二十二碳一烯酸': 'Erucic Acid',
    '二十二碳酸': 'Behenic Acid',
    '二十四碳一烯酸': 'Nervonic Acid',
    '二十四碳酸': 'Lignocerric Acid',
    'ω-3/ω-6 比值': 'w3/w6',
    '三烯/四烯比': 'Triene /Tetraene',
    '总饱和脂肪酸': 'Total SFA',
    '总单不饱和脂肪酸': 'Total MUFA',
    '总多不饱和脂肪酸': 'Total PUFA',
    '总 ω-3': 'T w3',
    '总 ω-6': 'T w6',
    '总脂肪酸': 'Total FA'
}

# English aliases for normalization (helpful if payload uses "Age" instead of "Year", etc.)
english_aliases = {
    "Age": "Year",
    "Decanoic Acid": "Decanoric Acid",
    "Lauric Acid": "Laurric Acid",
    "Myristic Acid": "Myristic Acid", # Careful: 肉豆蔻酸 is Myristoleic in SVM.py
    "Palmitic Acid": "Palmitic Acid",
    "Eicosapentaenoic Acid": "EPA",
    "Docosahexaenoic Acid": "DHA",
    "Docosapentaenoic Acid": "DPA",
    "Docosatetraenoic Acid": "DTA",
    "Lignoceric Acid": "Lignocerric Acid",
    "Triene/Tetraene": "Triene /Tetraene",
}

def to_float(v):
    try:
        if v is None or v == "": return 0.0
        return float(v)
    except (TypeError, ValueError):
        return 0.0

def build_processed_features(payload: dict):
    """
    Converts incoming payload into a canonical {EnglishFeatureName: float} dict.
    We prioritize Chinese mapping, then English aliases.
    """
    processed = {}

    # 1. Start with English keys in payload (normalized)
    for k, v in payload.items():
        if k in ("result", "label", "target", "modelType", "userId", "id"):
            continue
        canonical = english_aliases.get(k, k)
        processed[canonical] = to_float(v)

    # 2. Overwrite/add with Chinese mapping (canonical)
    for cn_key, eng_key in cn_to_eng.items():
        if cn_key in payload:
            processed[eng_key] = to_float(payload.get(cn_key))

    return processed

# Fallback feature list based on SVM.py training
CANONICAL_FEATURES = [
    'Sex', 'Year', 'Decanoric Acid', 'Laurric Acid', 'Myristoleic Acid',
    'Myristic Acid', 'Palmitoleic Acid', 'Palmitic Acid', 'Linolenic Acid',
    'Linoleic Acid', 'Oleic Acid', 'Stearic Acid', 'EPA', 'Arachidonic Acid',
    'Mead Acid', 'Eicosadienoic Acid', 'Eicosenoic Acid', 'Arachidic Acid',
    'DHA', 'DPA', 'DTA', 'Erucic Acid', 'Behenic Acid', 'Nervonic Acid',
    'Lignocerric Acid', 'w3/w6', 'Triene /Tetraene', 'Total SFA', 'Total MUFA',
    'Total PUFA', 'T w3', 'T w6', 'Total FA'
]

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
    logger.info(f"Loaded {model_type} from {filename} | features={len(feature_names) if feature_names else 'None'}")
    return MODEL_CACHE[model_type]

def predict_probability(model, X):
    # Some models (like XGBoost) might require DMatrix or specific input types
    # but sklearn-wrapped ones work with numpy.
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

        # 1. Determine feature order
        if feature_names:
            ordered_keys = list(feature_names)
        else:
            logger.warning(f"Model {model_type} has no saved feature_names. Using CANONICAL_FEATURES.")
            ordered_keys = CANONICAL_FEATURES

        # 2. Build input vector
        X = np.array([[processed.get(name, 0.0) for name in ordered_keys]], dtype=float)

        # 3. Apply scaler if present
        if scaler is not None:
            try:
                X = scaler.transform(X)
            except Exception as se:
                logger.error(f"Scaling failed: {se}. Vector shape: {X.shape}")
                # Fallback: maybe the scaler expects a different number of features?
                # For now, we'll re-raise to see the error.
                raise se

        # 4. Predict
        prob = predict_probability(model, X)

        # Basic label (optional, but your UI expects disease/suggestion)
        disease = "Diabetes" if prob >= 0.5 else "Normal"
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
        # Return more diagnostic info to the frontend
        error_info = {
            "error": str(e),
            "step": "prediction_process",
            "modelType": model_type if 'model_type' in locals() else "unknown"
        }
        
        # Add shape info if we got that far
        if 'X' in locals():
            error_info["input_shape"] = list(X.shape)
        if 'ordered_keys' in locals():
            error_info["expected_features"] = len(ordered_keys)
        if 'processed' in locals():
            error_info["received_features"] = len(processed)
            
        return jsonify(error_info), 500

@app.route("/api/predict_all", methods=["POST"])
def predict_all():
    """
    Returns predictions from all 5 models for comparison.
    """
    try:
        payload = request.get_json(force=True) or {}
        processed = build_processed_features(payload)
        
        results = {}
        for m_type in MODEL_FILES.keys():
            try:
                model, scaler, feature_names = load_model_by_type(m_type)
                
                if feature_names:
                    ordered_keys = list(feature_names)
                else:
                    ordered_keys = CANONICAL_FEATURES
                
                X = np.array([[processed.get(name, 0.0) for name in ordered_keys]], dtype=float)
                
                if scaler is not None:
                    X = scaler.transform(X)
                
                prob = predict_probability(model, X)
                
                results[m_type] = {
                    "probability": prob,
                    "percentage": prob * 100.0,
                    "label": "Diabetes" if prob >= 0.5 else "Normal"
                }
            except Exception as me:
                logger.error(f"Failed prediction for {m_type}: {me}")
                results[m_type] = {"error": str(me)}
                
        return jsonify({
            "results": results,
            "count": len(results)
        })
    except Exception as e:
        logger.error(f"Error during predict_all: {e}", exc_info=True)
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5000, debug=True)
