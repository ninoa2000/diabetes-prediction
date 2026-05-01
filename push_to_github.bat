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

echo --- Initializing/Checking Git Repository ---
if not exist .git (
    echo Initializing new repository...
    git init
)

echo --- Checking Remote Configuration ---
git remote get-url origin >nul 2>&1
if %errorlevel% neq 0 (
    echo Adding remote origin...
    git remote add origin %repo_url%
) else (
    echo Remote origin already exists. Updating URL...
    git remote set-url origin %repo_url%
)

echo --- Stage Changes ---
git add .

echo --- Committing Changes ---
git commit -m "Completed full English localization, interactive UI improvements, and final cleanup"

echo --- Pushing to GitHub ---
git branch -M main
git push -u origin main --force

echo.
echo --- Process Finished ---
if %errorlevel% neq 0 (
    echo.
    echo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    echo ERROR: The push failed. This often happens on Windows/OneDrive.
    echo PLEASE TRY THE FOLLOWING:
    echo 1. Pause OneDrive syncing temporarily.
    echo 2. Close any other programs using this folder.
    echo 3. Run this script again.
    echo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
)
echo.
pause
