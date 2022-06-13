@echo off
echo -----------------------------------------------------------------------
echo Construir imagen del proyecto transaction-authorizer
docker build --tag=oscaragr1990/transaction-authorizer:test .
echo -----------------------------------------------------------------------
echo Subir Imagen al repositorio
docker push oscaragr1990/transaction-authorizer:test
