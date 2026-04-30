@echo off
echo Starting Backend (Spring Boot)...
start cmd /k "cd backend && mvn spring-boot:run"

echo Starting Frontend (Vite)...
start cmd /k "cd frontend && npm install && npm run dev"

echo Starting Prediction API (Flask)...
start cmd /k "cd prediction && pip install -r requirements.txt && python app.py"

echo All services are starting in separate windows.
