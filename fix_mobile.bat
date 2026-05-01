@echo off
echo ==========================================
echo FIXING MOBILE APP FOR DISSERTATION
echo ==========================================
echo 1. Cleaning up old broken files...
cd mobile
rd /s /q node_modules
del package-lock.json
echo 2. Installing SDK 54 (Fresh)...
call npm install
echo 3. Starting App with Tunnel...
npx expo start --tunnel
pause
