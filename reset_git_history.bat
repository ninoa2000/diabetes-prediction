@echo off
setlocal enabledelayedexpansion

echo ======================================================
echo           GIT HISTORY RESET TOOL (v2)
echo ======================================================
echo.
echo IMPORTANT: 
echo 1. PAUSE OneDrive syncing before proceeding.
echo 2. Close your IDE (VS Code, etc.) if possible.
echo.

set /p confirm="Are you ready to proceed? (Y/N): "
if /i "%confirm%" neq "Y" (
    echo Operation cancelled.
    pause
    exit /b
)

echo.
echo [1/6] Enabling Long Path support...
git config core.longpaths true

echo [2/6] Cleaning up any messy state...
:: Force return to main to start clean
git checkout main -f >nul 2>&1
:: Remove everything from index (but keep files)
git rm -r --cached . >nul 2>&1

echo [3/6] Creating fresh history...
git checkout --orphan temp_branch

echo [4/6] Staging files (ignoring node_modules)...
git add -A

echo [5/6] Creating initial commit...
git commit -m "Initial project state"

echo [6/6] Updating branch structure...
git branch -D main
git branch -m main

echo.
echo [FINAL STEP] Force pushing to GitHub...
echo (This will replace EVERYTHING on GitHub with your current files)
set /p pushconfirm="Push to GitHub now? (Y/N): "
if /i "%pushconfirm%" == "Y" (
    git push -f origin main
)

echo.
echo ======================================================
echo DONE!
echo If you saw errors about "Deletion of directory failed",
echo it's likely OneDrive. Just run the script again with
echo OneDrive PAUSED.
echo ======================================================
echo.
pause
