@echo off
echo -----------------------------------------------------------------------
echo Descargar Imagen al repositorio
docker pull oscaragr1990/transaction-authorizer:test
echo -----------------------------------------------------------------------
echo Ejecutar Imagen 
docker run -p8080:8080 oscaragr1990/transaction-authorizer:test