import joblib
import os
import numpy as np

MODEL_FILES = {
    "svm": "svm_complete_model.pkl",
    "xgboost": "xgboost_complete_model.pkl",
    "random_forest": "random_forest_complete_model.pkl",
    "mlp": "mlp_complete_model.pkl",
    "decision_tree": "decision_tree_complete_model.pkl",
}

def test_models():
    for name, filename in MODEL_FILES.items():
        print(f"Testing {name} ({filename})...")
        if not os.path.exists(filename):
            print(f"  FAILED: {filename} does not exist.")
            continue
        try:
            raw = joblib.load(filename)
            print(f"  SUCCESS: Loaded {filename}")
            if isinstance(raw, dict):
                model = raw.get("model") or raw.get("clf") or raw.get("pipeline") or raw
                scaler = raw.get("scaler")
                feature_names = raw.get("feature_names") or raw.get("columns")
                print(f"    Model type: {type(model)}")
                print(f"    Scaler type: {type(scaler)}")
                print(f"    Feature names count: {len(feature_names) if feature_names else 'None'}")
                if feature_names:
                    print(f"    First 5 features: {feature_names[:5]}")
            else:
                print(f"    Model type: {type(raw)}")
        except Exception as e:
            print(f"  FAILED: Could not load {filename}: {e}")

if __name__ == "__main__":
    test_models()
