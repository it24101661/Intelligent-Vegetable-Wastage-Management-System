@echo off
set "ROOT=%~dp0"

echo ===================================================
echo   VegLife - Starting All Microservices
echo   Root Directory: %ROOT%
echo ===================================================

:: 1. Farmer Backend
echo [1/5] Starting Farmer Backend (8080)...
start "Farmer_Backend" cmd /k "cd /d "%ROOT%FARMERMANAGEMENT - BACKEND" && mvn spring-boot:run"

:: 2. Delivery Backend
echo [2/5] Starting Delivery Backend (8082)...
start "Delivery_Backend" cmd /k "cd /d "%ROOT%Delivery and Distribution" && mvn spring-boot:run"

:: 3. Payment Backend
echo [3/5] Starting Payment Backend (8081)...
start "Payment_Backend" cmd /k "cd /d "%ROOT%Intelligent-Vegetable-Wastage-Management-System-Payment-Transaction-Management\Payment" && mvn spring-boot:run"

:: 6. Product Listing Backend (8083)
echo [6/6] Starting Product Listing Backend (8083)...
start "Product_Listing" cmd /k "cd /d "%ROOT%vegwaste-backend" && mvn spring-boot:run"

:: 4. AI Service
echo [4/5] Starting AI Service (8000)...
start "AI_Service" cmd /k "cd /d "%ROOT%" && python -m uvicorn main:app --port 8000"

:: 5. React Frontend
echo [5/5] Starting React Frontend...
start "React_Frontend" cmd /k "cd /d "%ROOT%veglife_react_frontend\veglife-react" && npm run dev"

echo.
echo Check the new windows for progress...
echo ===================================================
pause
