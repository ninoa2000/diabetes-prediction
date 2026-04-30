@echo off
set repo_url=https://github.com/ninoa2000/diabetes-prediction.git

echo --- Checking Git Installation ---
git --version
if %errorlevel% neq 0 (
    echo ERROR: Git is not installed on your computer! 
    echo Please install it from https://git-scm.com/
    pause
    exit /b
)

echo --- Initializing Git ---
git init

echo --- Removing old remote (if any) ---
git remote remove origin >nul 2>&1

echo --- Setting remote to %repo_url% ---
git remote add origin %repo_url%

echo --- Adding files ---
git add .

echo --- Committing ---
git commit -m "Added AI algorithm and filename tracking to medical records"

echo --- Pushing to GitHub (Main Branch) ---
git branch -M main
git push -u origin main

echo.
echo --- Process Finished ---
echo If you see errors above about "Authentication" or "Permission denied", 
echo it means you need to log in to GitHub in this window.
echo.
pause
