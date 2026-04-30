import pandas as pd
import os

files = ['real_diabetic_patient.xlsx', 'real_healthy_patient.xlsx']
for f in files:
    if os.path.exists(f):
        df = pd.read_excel(f)
        print(f"File: {f} | Rows: {len(df)} | Columns: {len(df.columns)}")
    else:
        print(f"File: {f} NOT FOUND")
