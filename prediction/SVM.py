import pandas as pd
import numpy as np
import re
from sklearn.svm import SVC
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, confusion_matrix, roc_auc_score, f1_score, recall_score, roc_curve, auc
from sklearn.preprocessing import StandardScaler
import matplotlib.pyplot as plt
import seaborn as sns
import joblib

# ===============================
# 1. Load dataset
# ===============================
df = pd.read_excel("dataset.xlsx")

# ===============================
# 2. Rename columns to MATCH Flask app
# ===============================
rename_map = {
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

df = df.rename(columns=rename_map)

# Clean numeric strings
def clean_numeric_str(x):
    if isinstance(x, str):
        x = x.replace("＜", "<").replace("＞", ">").strip()
        if x in ["", ".", "-", "NA", "N/A", "na", "<", ">", "—"]:
            return np.nan
        if x.startswith("<"):
            nums = re.findall(r"\d+(\.\d+)?", x)
            if nums and nums[0] != "":
                return float(nums[0]) * 0.5
            return np.nan
        if x.startswith(">"):
            nums = re.findall(r"\d+(\.\d+)?", x)
            if nums and nums[0] != "":
                return float(nums[0]) * 1.5
            return np.nan
        return float(x) if x.replace('.', '', 1).isdigit() else np.nan
    return x

# Clean numeric columns
numeric_cols = df.select_dtypes(include=['object']).columns
for col in numeric_cols:
    if col != 'result':  # Don't clean the target column
        df[col] = df[col].apply(clean_numeric_str)

# Convert to numeric
for col in df.columns:
    if col != 'result':
        df[col] = pd.to_numeric(df[col], errors='coerce')

# Fill missing values
numeric_cols_all = df.select_dtypes(include=[np.number]).columns.drop('result', errors='ignore')
df[numeric_cols_all] = df[numeric_cols_all].fillna(df[numeric_cols_all].median())

# ===============================
# 3. Split X / y
# ===============================
X = df.drop(columns=['result'])
y = df['result']

X_train, X_test, y_train, y_test = train_test_split(
    X,
    y,
    test_size=0.2,
    random_state=42,
    stratify=y
)

print("SVM Training")
print("=" * 50)

# ===============================
# 4. Scale features (important for SVM)
# ===============================
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)
print("Features scaled successfully")

# ===============================
# 5. Train SVM model
# ===============================
model = SVC(
    random_state=42,
    probability=True,  # Enable probability predictions for ROC-AUC
    kernel='rbf',
    C=1.0,
    class_weight='balanced'  # Handle class imbalance
)

model.fit(X_train_scaled, y_train)
print("SVM model trained successfully")

# ===============================
# 6. Evaluation
# ===============================
y_pred = model.predict(X_test_scaled)
y_pred_proba = model.predict_proba(X_test_scaled)[:, 1]

accuracy = accuracy_score(y_test, y_pred)
f1 = f1_score(y_test, y_pred)
roc_auc = roc_auc_score(y_test, y_pred_proba)
recall = recall_score(y_test, y_pred)

print(f"\nAccuracy:  {accuracy:.4f}")
print(f"F1 Score:  {f1:.4f}")
print(f"ROC AUC:   {roc_auc:.4f}")
print(f"Recall:    {recall:.4f}")

# ===============================
# 7. Confusion Matrix
# ===============================
cm = confusion_matrix(y_test, y_pred)
TN, FP, FN, TP = cm.ravel()

print("\nConfusion Matrix")
print(f"TN: {TN}, FP: {FP}, FN: {FN}, TP: {TP}")

plt.figure(figsize=(8, 6))
sns.heatmap(
    cm,
    annot=True,
    fmt='d',
    cmap='Blues',
    xticklabels=['Predicted 0', 'Predicted 1'],
    yticklabels=['Actual 0', 'Actual 1']
)

plt.title('SVM Confusion Matrix')
plt.xlabel('Predicted Label')
plt.ylabel('True Label')
plt.show()

# ===============================
# 8. ROC Curve
# ===============================
fpr, tpr, _ = roc_curve(y_test, y_pred_proba)
roc_auc = auc(fpr, tpr)

plt.figure(figsize=(8, 6))
plt.plot(fpr, tpr, color='darkorange', lw=2, label=f'ROC curve (AUC = {roc_auc:.2f})')
plt.plot([0, 1], [0, 1], color='navy', lw=2, linestyle='--')
plt.xlim([0.0, 1.0])
plt.ylim([0.0, 1.05])
plt.xlabel('False Positive Rate')
plt.ylabel('True Positive Rate')
plt.title('ROC Curve - SVM')
plt.legend(loc="lower right")
plt.grid(True, alpha=0.3)
plt.show()

# ===============================
# 9. Save model and scaler
# ===============================
# Save the SVM model
joblib.dump(model, "svm_model.pkl")
print("Model saved as svm_model.pkl")

# Save the scaler
joblib.dump(scaler, "svm_scaler.pkl")
print("Scaler saved as svm_scaler.pkl")

# Save feature names
feature_names = list(X.columns)
joblib.dump(feature_names, "svm_feature_names.pkl")
print("Feature names saved as svm_feature_names.pkl")

# ===============================
# 10. Create a dictionary with all model info
# ===============================
model_info = {
    'model': model,
    'scaler': scaler,
    'feature_names': feature_names,
    'accuracy': accuracy,
    'f1_score': f1,
    'roc_auc': roc_auc,
    'recall': recall
}

# Save complete model package
joblib.dump(model_info, "svm_complete_model.pkl")
print("Complete model package saved as svm_complete_model.pkl")

print("\nSVM model training complete!")
print(f"Model saved as: svm_model.pkl")
print(f"Complete package saved as: svm_complete_model.pkl")

# Print feature names
print("\nModel feature names:")
for i, feature in enumerate(feature_names, 1):
    print(f"{i:2}. {feature}")