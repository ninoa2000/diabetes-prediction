"""
Fix passwords script - resets user passwords in MySQL to known values.
Run from the project root directory.
"""
import subprocess
import sys

# Install bcrypt if not available
try:
    import bcrypt
except ImportError:
    subprocess.check_call([sys.executable, "-m", "pip", "install", "bcrypt"])
    import bcrypt

import mysql.connector

# ---- Config ----
MYSQL_HOST = "localhost"
MYSQL_USER = "root"
MYSQL_PASSWORD = "admin123"
MYSQL_DB = "db_health"

# Users to reset and their new passwords
RESET_USERS = {
    "ninoa2000": "admin123",
    "admin":     "admin123",
}

def hash_password(plain: str) -> str:
    salt = bcrypt.gensalt(rounds=10)
    return bcrypt.hashpw(plain.encode(), salt).decode()

def main():
    print("Connecting to MySQL...")
    try:
        conn = mysql.connector.connect(
            host=MYSQL_HOST,
            user=MYSQL_USER,
            password=MYSQL_PASSWORD,
            database=MYSQL_DB
        )
    except Exception as e:
        print(f"❌ Could not connect to MySQL: {e}")
        sys.exit(1)

    cursor = conn.cursor()
    print("✅ Connected.\n")

    for username, new_password in RESET_USERS.items():
        # Check if user exists
        cursor.execute("SELECT id FROM users WHERE username = %s", (username,))
        row = cursor.fetchone()
        if not row:
            print(f"⚠️  User '{username}' not found in MySQL, skipping.")
            continue

        # Generate fresh BCrypt hash
        new_hash = hash_password(new_password)
        cursor.execute(
            "UPDATE users SET password = %s WHERE username = %s",
            (new_hash, username)
        )
        conn.commit()
        print(f"✅ Reset password for '{username}' → new password: '{new_password}'")

    cursor.close()
    conn.close()
    print("\nDone! Try logging in with the passwords shown above.")

if __name__ == "__main__":
    main()
