@echo off
echo Learning Tracker Application - Startup Script

echo.
echo IMPORTANT: The duplicate backend code in the root src directory has been renamed to src_outdated.
echo Please use only the code in the backend and frontend directories.
echo.

echo Starting from the correct directories...
echo.

echo 1. Start the backend (Spring Boot)
echo    Navigate to the backend directory and run:
echo    cd backend
echo    ./gradlew bootRun
echo.
echo 2. Start the frontend (React)
echo    Navigate to the frontend directory and run:
echo    cd frontend
echo    npm install
echo    npm run dev
echo.
echo 3. Access the application
echo    Backend API: http://localhost:8080
echo    Frontend UI: http://localhost:5173
echo    API Documentation: http://localhost:8080/swagger-ui.html
echo.

pause
