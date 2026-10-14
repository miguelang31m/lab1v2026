@echo off
setlocal
cd /d "%~dp0"
echo ==========================================
echo LAB 2 - DOCKER CLOUD DEMO
 echo ==========================================
docker build -t lab1v2026:lab2 .
if errorlevel 1 goto :error
docker rm -f lab1v2026-demo >nul 2>&1
docker run --name lab1v2026-demo -p 8080:8080 -e SPRING_PROFILES_ACTIVE=cloud lab1v2026:lab2
exit /b 0
:error
echo ERROR construyendo la imagen Docker.
pause
exit /b 1
