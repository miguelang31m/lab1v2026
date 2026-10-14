@echo off
setlocal
cd /d "%~dp0"
echo ==========================================
echo LAB 2 - PRUEBAS + COBERTURA JACOCO
echo ==========================================
call mvnw.cmd clean verify
if errorlevel 1 (
  echo.
  echo ERROR: las pruebas o la compilacion fallaron.
  pause
  exit /b 1
)
echo.
echo OK: pruebas finalizadas correctamente.
echo Reporte: target\site\jacoco\index.html
if exist "target\site\jacoco\index.html" start "" "target\site\jacoco\index.html"
pause
