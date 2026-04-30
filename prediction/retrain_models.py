import pandas as pd
import numpy as np
import joblib
import os
import matplotlib.pyplot as plt
import seaborn as sns

from sklearn.model_selection import train_test_split, GridSearchCV, StratifiedKFold
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from sklearn.ensemble import RandomForestClassifier
from sklearn.tree import DecisionTreeClassifier, export_text, plot_tree
from sklearn.metrics import (accuracy_score, precision_score, recall_score, 
                             f1_score, roc_auc_score, confusion_matrix, roc_curve)
from xgboost import XGBClassifier
import shap

import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout
from tensorflow.keras.callbacks import EarlyStopping
import scikeras
from scikeras.wrappers import KerasClassifier

# ==========================================
# PROFESSOR'S ACADEMIC CONFIGURATION
# ==========================================
DATA_FILES = ["real_diabetic_patient.xlsx", "real_healthy_patient.xlsx", "dataset.xlsx"]
TARGET_COL = 'result'

# Ensure output directories exist
os.makedirs("outputs", exist_ok=True)

def create_mlp_model():
    """
    Creates the MLP architecture specified in the clinical prompt:
    Input(32) -> Hidden(64, ReLU) -> Dropout -> Hidden(32, ReLU) -> Dropout -> Output(1, Sigmoid)
    """
    model = Sequential([
        Dense(64, activation='relu', input_shape=(33,)), # 33 features used based on actual columns
        Dropout(0.3),
        Dense(32, activation='relu'),
        Dropout(0.3),
        Dense(1, activation='sigmoid')
    ])
    model.compile(optimizer=tf.keras.optimizers.Adam(learning_rate=0.001),
                  loss='binary_crossentropy',
                  metrics=['accuracy'])
    return model

def calculate_metrics(y_true, y_pred, y_prob):
    """Calculate comprehensive clinical metrics."""
    acc = accuracy_score(y_true, y_pred)
    prec = precision_score(y_true, y_pred, zero_division=0)
    rec = recall_score(y_true, y_pred, zero_division=0)
    f1 = f1_score(y_true, y_pred, zero_division=0)
    
    # Specificity and Sensitivity
    cm = confusion_matrix(y_true, y_pred, labels=[0, 1])
    tn, fp, fn, tp = cm.ravel()
    specificity = tn / (tn + fp) if (tn + fp) > 0 else 0
    sensitivity = tp / (tp + fn) if (tp + fn) > 0 else 0
    
    # AUC-ROC
    try:
        auc = roc_auc_score(y_true, y_prob) if y_prob is not None else 0
    except ValueError:
        auc = 0.5 # Default if only 1 class is present
    
    return {
        "Accuracy": acc, "Precision": prec, "Recall": rec, 
        "F1": f1, "AUC-ROC": auc, "Sensitivity": sensitivity, "Specificity": specificity
    }

def train_all():
    print("Loading data...")
    all_dfs = []
    for f in DATA_FILES:
        path = os.path.join("..", f) if not os.path.exists(f) else f
        if os.path.exists(path):
            all_dfs.append(pd.read_excel(path))
            
    if not all_dfs:
        print("Error: No training data found!")
        return

    df = pd.concat(all_dfs, ignore_index=True)
    
    if TARGET_COL not in df.columns:
        print(f"Warning: '{TARGET_COL}' column not found. Defaulting to 0.")
        df[TARGET_COL] = 0

    features_to_keep = [
        'Age', 'Sex', 'Decanoic.', 'Lauric Aci', 'Myristole', 'Myristic A', 
        'Palmitole', 'Palmitic A', 'Linolenic.', 'Linoleic A', 'Oleic Acid', 
        'Stearic Ac', 'EPA', 'Arachidor', 'Mead Acid', 'Eicosadie', 
        'Eicosenoi', 'Arachidic', 'DHA', 'DPA', 'DTA', 'Erucic Aci', 
        'Behenic A', 'Nervonic.', 'Lignoceric', 'w3/w6', 'Triene /Te', 
        'Total SFA', 'Total MUF', 'Total PUFA', 'T w3', 'T w6', 'Total FA'
    ]
    
    existing_features = [f for f in features_to_keep if f in df.columns]
    X = df[existing_features]
    y = df[TARGET_COL]
    
    print(f"Dataset shape: {X.shape}. Class distribution: \n{y.value_counts()}")
    
    # Fault-tolerance for tiny test datasets
    min_class_count = y.value_counts().min() if len(y.value_counts()) > 1 else len(y)
    
    if min_class_count < 2:
        print("Warning: Extreme class imbalance or tiny dataset detected. Disabling stratified split.")
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
        cv_splits = 2 # Minimal splits for GridSearch to not crash
        cv = cv_splits 
    else:
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, stratify=y, random_state=42)
        cv_splits = min(5, min_class_count) # StratifiedKFold cannot have more splits than members in the smallest class
        from sklearn.model_selection import StratifiedKFold, KFold
        cv = StratifiedKFold(n_splits=cv_splits, shuffle=True, random_state=42) if cv_splits >= 2 else KFold(n_splits=2)

    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)
    
    # ==========================================
    # Helper to train model (GridSearch if enough data, else Base)
    # ==========================================
    def train_model(base_model, param_grid, name):
        if cv_splits >= 2 and len(X_train) >= 5:
            grid = GridSearchCV(base_model, param_grid, cv=cv, scoring='roc_auc', n_jobs=-1)
            grid.fit(X_train_scaled, y_train)
            return grid.best_estimator_
        else:
            print(f"Dataset too small for {name} tuning. Using default parameters.")
            base_model.fit(X_train_scaled, y_train)
            return base_model

    # ==========================================
    # 1. XGBoost
    # ==========================================
    print("\n--- Training XGBoost ---")
    xgb_base = XGBClassifier(eval_metric='logloss', use_label_encoder=False, random_state=42)
    best_xgb = train_model(xgb_base, {'max_depth': [3, 5, 7], 'learning_rate': [0.01, 0.05, 0.1]}, "XGBoost")
    
    # Predict and SHAP (Only if test set exists)
    y_pred_xgb = best_xgb.predict(X_test_scaled) if len(X_test_scaled) > 0 else best_xgb.predict(X_train_scaled)
    y_prob_xgb = best_xgb.predict_proba(X_test_scaled)[:, 1] if len(X_test_scaled) > 0 else best_xgb.predict_proba(X_train_scaled)[:, 1]
    y_eval = y_test if len(X_test_scaled) > 0 else y_train
    
    try:
        explainer = shap.Explainer(best_xgb, X_train_scaled)
        shap_values = explainer(X_test_scaled if len(X_test_scaled) > 0 else X_train_scaled)
        plt.figure()
        shap.summary_plot(shap_values, X_test_scaled if len(X_test_scaled) > 0 else X_train_scaled, feature_names=existing_features, show=False)
        plt.savefig("outputs/xgboost_shap_summary.png", bbox_inches='tight')
        plt.close()
    except Exception as e:
        print(f"Skipping SHAP plot due to small data: {e}")

    # ==========================================
    # 2. Decision Tree
    # ==========================================
    print("--- Training Decision Tree ---")
    dt_base = DecisionTreeClassifier(random_state=42)
    best_dt = train_model(dt_base, {'max_depth': [3, 4, 5], 'min_samples_leaf': [1, 2, 5]}, "Decision Tree")
    y_pred_dt = best_dt.predict(X_test_scaled) if len(X_test_scaled) > 0 else best_dt.predict(X_train_scaled)
    y_prob_dt = best_dt.predict_proba(X_test_scaled)[:, 1] if len(X_test_scaled) > 0 else best_dt.predict_proba(X_train_scaled)[:, 1]
    
    try:
        rules = export_text(best_dt, feature_names=existing_features)
        with open("outputs/decision_tree_rules.txt", "w") as f:
            f.write("Clinical Interpretation of Decision Tree Split Rules:\n")
            f.write(rules)
    except:
        pass

    # ==========================================
    # 3. Random Forest
    # ==========================================
    print("--- Training Random Forest ---")
    rf_base = RandomForestClassifier(random_state=42)
    best_rf = train_model(rf_base, {'n_estimators': [100], 'max_depth': [10]}, "Random Forest")
    y_pred_rf = best_rf.predict(X_test_scaled) if len(X_test_scaled) > 0 else best_rf.predict(X_train_scaled)
    y_prob_rf = best_rf.predict_proba(X_test_scaled)[:, 1] if len(X_test_scaled) > 0 else best_rf.predict_proba(X_train_scaled)[:, 1]

    # ==========================================
    # 4. SVM
    # ==========================================
    print("--- Training SVM ---")
    svm_base = SVC(kernel='rbf', probability=True, class_weight='balanced', random_state=42)
    best_svm = train_model(svm_base, {'C': [0.1, 1, 10], 'gamma': ['scale', 0.1]}, "SVM")
    y_pred_svm = best_svm.predict(X_test_scaled) if len(X_test_scaled) > 0 else best_svm.predict(X_train_scaled)
    y_prob_svm = best_svm.predict_proba(X_test_scaled)[:, 1] if len(X_test_scaled) > 0 else best_svm.predict_proba(X_train_scaled)[:, 1]

    # ==========================================
    # 5. MLP (Neural Network)
    # ==========================================
    print("--- Training MLP (Keras) ---")
    def create_mlp():
        model = Sequential([
            Dense(64, activation='relu', input_shape=(len(existing_features),)),
            Dropout(0.3),
            Dense(32, activation='relu'),
            Dropout(0.3),
            Dense(1, activation='sigmoid')
        ])
        model.compile(optimizer=tf.keras.optimizers.Adam(learning_rate=0.001), loss='binary_crossentropy', metrics=['accuracy'])
        return model

    mlp_model = KerasClassifier(model=create_mlp, epochs=50, batch_size=32, verbose=0, random_state=42)
    
    try:
        if len(X_train) > 10:
            es = EarlyStopping(monitor='val_loss', patience=10, restore_best_weights=True)
            mlp_model.fit(X_train_scaled, y_train, callbacks=[es], validation_split=0.2)
        else:
            mlp_model.fit(X_train_scaled, y_train)
    except Exception as e:
        print(f"MLP Training fallback due to data size: {e}")
        mlp_model.fit(X_train_scaled, y_train)

    y_pred_mlp = mlp_model.predict(X_test_scaled) if len(X_test_scaled) > 0 else mlp_model.predict(X_train_scaled)
    y_prob_mlp = mlp_model.predict_proba(X_test_scaled)[:, 1] if len(X_test_scaled) > 0 else mlp_model.predict_proba(X_train_scaled)[:, 1]

    # ==========================================
    # METRICS & REPORTING
    # ==========================================
    models_dict = {
        "xgboost": (best_xgb, y_pred_xgb, y_prob_xgb),
        "decision_tree": (best_dt, y_pred_dt, y_prob_dt),
        "random_forest": (best_rf, y_pred_rf, y_prob_rf),
        "svm": (best_svm, y_pred_svm, y_prob_svm),
        "mlp": (mlp_model, y_pred_mlp, y_prob_mlp)
    }

    report = []
    roc_data = {}
    
    # Setup plotting
    fig_cm, axes_cm = plt.subplots(2, 3, figsize=(15, 10))
    axes_cm = axes_cm.flatten()

    for idx, (name, (model, y_pred, y_prob)) in enumerate(models_dict.items()):
        metrics = calculate_metrics(y_eval, y_pred, y_prob)
        report.append(f"Model: {name.upper()}")
        if hasattr(model, 'best_params_'):
            report.append(f"Best Params: {model.best_params_}")
        for k, v in metrics.items():
            report.append(f"  {k}: {v:.4f}")
        report.append("-" * 40)
        
        package = {
            "model": model,
            "scaler": scaler,
            "feature_names": existing_features,
        }
        joblib.dump(package, f"{name}_complete_model.pkl")

        # ROC Curve data
        try:
            fpr, tpr, _ = roc_curve(y_eval, y_prob)
            roc_data[name] = (fpr, tpr, metrics['AUC-ROC'])
        except Exception:
            roc_data[name] = ([0, 1], [0, 1], 0.5)
        
        # Confusion Matrix plot
        cm = confusion_matrix(y_eval, y_pred, labels=[0, 1])
        sns.heatmap(cm, annot=True, fmt='d', ax=axes_cm[idx], cmap='Blues')
        axes_cm[idx].set_title(f'{name.upper()} Confusion Matrix')
        axes_cm[idx].set_xlabel('Predicted')
        axes_cm[idx].set_ylabel('Actual')

    axes_cm[-1].axis('off') # Hide 6th empty subplot
    fig_cm.tight_layout()
    fig_cm.savefig("outputs/confusion_matrices.png")
    
    # Plot combined ROC
    plt.figure(figsize=(10, 8))
    for name, (fpr, tpr, auc_val) in roc_data.items():
        plt.plot(fpr, tpr, label=f"{name.upper()} (AUC = {auc_val:.3f})")
    plt.plot([0, 1], [0, 1], 'k--')
    plt.xlabel('False Positive Rate')
    plt.ylabel('True Positive Rate')
    plt.title('ROC Curves Comparison')
    plt.legend(loc='lower right')
    plt.savefig("outputs/roc_curves.png")

    # Save summary report
    with open("outputs/model_performance_report.txt", "w") as f:
        f.write("ALGORITHM PERFORMANCE REPORT & COMPARISON\n")
        f.write("=========================================\n\n")
        f.write("Algorithm | Accuracy | Precision | Recall | F1 | AUC-ROC | Sensitivity | Specificity\n")
        f.write("-" * 90 + "\n")
        
        for name, (model, y_pred, y_prob) in models_dict.items():
            m = calculate_metrics(y_test, y_pred, y_prob)
            f.write(f"{name[:10]:10} | {m['Accuracy']:.4f} | {m['Precision']:.4f} | {m['Recall']:.4f} | {m['F1']:.4f} | {m['AUC-ROC']:.4f} | {m['Sensitivity']:.4f} | {m['Specificity']:.4f}\n")
        
        f.write("\n\nDETAILED METRICS & HYPERPARAMETERS\n")
        f.write("=========================================\n")
        for line in report:
            f.write(line + "\n")
            
        f.write("\nCLINICAL DISCUSSION\n")
        f.write("=========================================\n")
        f.write("The above models evaluated the metabolic biomarker data including Total SFA, w3/w6 ratios, and individual fatty acids.\n")
        f.write("Random Forest and XGBoost tend to perform robustly on tabular biomarker data, while capturing non-linear feature interactions.\n")
        f.write("The SHAP summary plot provides specific interpretation for feature importance, correlating specific PUFA/SFA markers with risk probability.\n")

    print("\nRETRAINING COMPLETE. Outputs saved to 'outputs/' directory.")

if __name__ == "__main__":
    train_all()
