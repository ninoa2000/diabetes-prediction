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
# Mapping: Excel Header -> Internal Canonical Name
# -----------------------------
# We keep these EXACTLY as they appear in your Excel screenshot
cn_to_eng = {
    'Age': 'Age',
    'Sex': 'Sex',
    'Decanoic.': 'Decanoic.',
    'Lauric Aci': 'Lauric Aci',
    'Myristole': 'Myristole',
    'Myristic A': 'Myristic A',
    'Palmitole': 'Palmitole',
    'Palmitic A': 'Palmitic A',
    'Linolenic.': 'Linolenic.',
    'Linoleic A': 'Linoleic A',
    'Oleic Acid': 'Oleic Acid',
    'Stearic Ac': 'Stearic Ac',
    'EPA': 'EPA',
    'Arachidor': 'Arachidor',
    'Mead Acid': 'Mead Acid',
    'Eicosadie': 'Eicosadie',
    'Eicosenoi': 'Eicosenoi',
    'Arachidic': 'Arachidic',
    'DHA': 'DHA',
    'DPA': 'DPA',
    'DTA': 'DTA',
    'Erucic Aci': 'Erucic Aci',
    'Behenic A': 'Behenic A',
    'Nervonic.': 'Nervonic.',
    'Lignoceric': 'Lignoceric',
    'w3/w6': 'w3/w6',
    'Triene /Te': 'Triene /Te',
    'Total SFA': 'Total SFA',
    'Total MUF': 'Total MUF',
    'Total PUFA': 'Total PUFA',
    'T w3': 'T w3',
    'T w6': 'T w6',
    'Total FA': 'Total FA'
}

def to_float(v):
    try:
        if v is None or v == "": return 0.0
        return float(str(v).replace(',', '').strip())
    except (TypeError, ValueError):
        return 0.0

def build_processed_features(payload: dict):
    """
    Directly maps payload keys using the cn_to_eng mapping.
    """
    processed = {}
    for k, v in payload.items():
        # Map if possible, otherwise keep original
        canonical = cn_to_eng.get(k, k)
        processed[canonical] = to_float(v)
    return processed

# Canonical feature list (Backup only)
CANONICAL_FEATURES = list(cn_to_eng.values())


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

        # 3. Apply scaler if present (Rule: StandardScaler for SVM/MLP, no scaling for tree-based models)
        tree_based_models = ["xgboost", "decision_tree", "random_forest", "rf", "dt", "xgb"]
        if scaler is not None and model_type not in tree_based_models:
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
