@echo off
cd /d "%~dp0"
where sbcl >nul 2>nul
if errorlevel 1 (
  echo SBCL Common Lisp is required.
  exit /b 1
)
sbcl --script lisp\maintenance\lock-codes.lisp %*
exit /b %errorlevel%
