@echo off
echo --- Cleaning up Repository ---
echo This will remove "junk" files (like node_modules) from Git tracking.
echo.

echo 1. Removing everything from Git tracking (locally only)...
git rm -r --cached .

echo 2. Re-adding files (this will use the .gitignore I created)...
git add .

echo 3. Committing the cleanup...
git commit -m "Cleanup: removed node_modules and other junk files from tracking"

echo 4. Pushing the clean version to GitHub...
git push origin main

echo.
echo --- DONE! ---
echo The "10k" number in your sidebar should disappear or drop significantly now.
pause
