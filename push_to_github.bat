@echo off
setlocal enabledelayedexpansion

:: Configuration
set REPO_URL=https://github.com/ninoa2000/diabetes-prediction.git
set BRANCH=main

echo ======================================================
echo    DIABETES SYSTEM - AUTOMATIC GITHUB PUSH
echo ======================================================
echo.

:: Check Git
git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Git is not installed. Please install Git first.
    pause
    exit /b
)

:: Handle .git folder
if not exist .git (
    echo [INFO] Initializing Git repository...
    git init
)

:: Remote Check
git remote get-url origin >nul 2>&1
if %errorlevel% neq 0 (
    echo [INFO] Adding remote origin: %REPO_URL%
    git remote add origin %REPO_URL%
) else (
    git remote set-url origin %REPO_URL%
)

:: Stage all files
echo [INFO] Staging all changes...
git add .

:: Commit Message
set "COMMIT_MSG=Final Dissertation Update: Full English Doctor Interface & Auth Fixes"
if "%~1" neq "" set "COMMIT_MSG=%~1"

echo [INFO] Committing with message: "%COMMIT_MSG%"
git commit -m "%COMMIT_MSG%"

:: Push
echo [INFO] Pushing to GitHub (Branch: %BRANCH%)...
git branch -M %BRANCH%
git push -u origin %BRANCH% --force

if %errorlevel% eq 0 (
    echo.
    echo ======================================================
    echo    SUCCESS: Project pushed to GitHub successfully!
    echo ======================================================
) else (
    echo.
    echo [ERROR] Push failed. 
    echo TIP: If you see "lock" errors, try pausing OneDrive syncing temporarily.
)

echo.
pause
