import pandas as pd
import os

DATA_FILES = ["real_diabetic_patient.xlsx", "real_healthy_patient.xlsx", "dataset.xlsx"]

all_dfs = []
for f in DATA_FILES:
    path = os.path.join("..", f) if not os.path.exists(f) else f
    if os.path.exists(path):
        all_dfs.append(pd.read_excel(path))

if all_dfs:
    df = pd.concat(all_dfs, ignore_index=True)
    print("Columns:", list(df.columns))
    print("Shape:", df.shape)
    if 'result' in df.columns:
        print("Result distribution:\n", df['result'].value_counts())
else:
    print("No data found")
