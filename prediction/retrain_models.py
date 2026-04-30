import pandas as pd
import numpy as np
import joblib
import os
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from sklearn.ensemble import RandomForestClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.neural_network import MLPClassifier
from xgboost import XGBClassifier
from sklearn.metrics import accuracy_score

# Configuration
DATASET_PATH = "dataset.xlsx"
MODELS_TO_TRAIN = ["svm", "random_forest", "decision_tree", "mlp", "xgboost"]

def train_all():
    if not os.path.exists(DATASET_PATH):
        print(f"Error: {DATASET_PATH} not found. Please place your training data in this folder.")
        return

    print(f"Loading dataset from {DATASET_PATH}...")
    df = pd.read_excel(DATASET_PATH)
    
    # Simple preprocessing (adjust based on your actual columns)
    # This assumes the target column is named 'result' or 'target'
    target_col = 'result' if 'result' in df.columns else 'target'
    if target_col not in df.columns:
        print(f"Error: Could not find target column ('result' or 'target').")
        return

    X = df.drop(columns=[target_col])
    y = df[target_col]
    
    # Feature names for persistence
    feature_names = list(X.columns)
    
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
    
    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)
    
    models = {
        "svm": SVC(probability=True, random_state=42),
        "random_forest": RandomForestClassifier(random_state=42),
        "decision_tree": DecisionTreeClassifier(random_state=42),
        "mlp": MLPClassifier(random_state=42, max_iter=500),
        "xgboost": XGBClassifier(random_state=42)
    }
    
    for name, model in models.items():
        print(f"Training {name}...")
        # Trees and XGBoost don't strictly need scaling, but we'll use it for consistency here
        # or you can choose to use unscaled data for them.
        model.fit(X_train_scaled, y_train)
        
        y_pred = model.predict(X_test_scaled)
        acc = accuracy_score(y_test, y_pred)
        print(f"  Accuracy: {acc:.4f}")
        
        # Save complete package
        package = {
            "model": model,
            "scaler": scaler,
            "feature_names": feature_names,
            "accuracy": acc
        }
        filename = f"{name}_complete_model.pkl"
        joblib.dump(package, filename)
        print(f"  Saved to {filename}")

if __name__ == "__main__":
    train_all()
