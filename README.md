# TransactionAuthorizer-

## Requisitos

-  IDE como Visual Studio Code,STS, etc
- Herramienta para realizar peticiones API's - Postman 
- Git
- Docker desktop

## Proceso de Instalación

- Se descargar el código fuente del proyecto con el siguiente comando : git clone https://github.com/oscaragr1990/TransactionAuthorizer-.git 

#### Instalación 01
- Se sube el proyecto con puerto ":8080" a través de IDE del requisito.

#### Instalación 02
- Se ejecutar el archivo deploy-imagen.cmd (TransactionAuthorizer-/deploy-imagen.cmd)
	* Este archivo permite descargar la imagen oscaragr1990/transaction-authorizer:test y levanta la imagen con el puerto 8080


## Construcción de la imagen
- Se debe compilar el proyecto (mvn clean package), mediante el IDE deseado
- Se debe ejecutar el archivo build-image.cmd
  * Este archivo contiene los comandos para construir la imagen, y subir la imagen al repositorio (https://hub.docker.com/repository/docker/oscaragr1990/transaction-authorizer)

NOTA: Se debe esta logueado en la plataforma de https://hub.docker.com/


## Formas para verificación

- Ingresar al aplicativo POSTMAN e importar el archivo "ayesa.postman_collection.json".
- El archivo importado contiene 4 ejemplos de peticiones.
	* put-tx : Ejemplo para inserta una cuenta
	* put-account: Ejemplo para inserta una tx
	* get-account: Consulta las cuentas existentes 
	* get-account-tx: Consulta las transacciones existentes de una cuenta 
