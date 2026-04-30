import os
import pandas as pd
import glob

def find_data():
    print("--- Searching for Potential Training Data ---")
    files = glob.glob("**/*.xlsx", recursive=True) + glob.glob("**/*.csv", recursive=True)
    for f in files:
        try:
            if "node_modules" in f or "target" in f: continue
            df = None
            if f.endswith('.xlsx'):
                df = pd.read_excel(f, nrows=5)
            else:
                df = pd.read_csv(f, nrows=5)
            
            print(f"\nFile: {f}")
            print(f"Rows: {len(df)} (peek), Columns: {df.columns.tolist()}")
            if 'result' in df.columns or 'target' in df.columns:
                print(">> [MATCH] This looks like a labeled training dataset!")
        except Exception as e:
            pass

if __name__ == "__main__":
    find_data()
