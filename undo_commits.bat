@echo off
echo --- Undoing Local Commits ---
echo This will reset your local project to exactly match what is on GitHub.
echo ANY LOCAL COMMITS OR UNPUSHED CHANGES WILL BE DELETED.
echo.
set /p confirm="Are you sure you want to proceed? (Y/N): "
if /i "%confirm%" neq "Y" (
    echo Operation cancelled.
    pause
    exit /b
)

echo.
echo --- Fetching from GitHub ---
git fetch origin

echo --- Resetting to origin/main ---
git reset --hard origin/main

echo.
echo --- Success! Local state reset to origin/main ---
echo.
pause
