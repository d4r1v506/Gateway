# API GATEWAY
## Descripción

Este documento describe la configuración y so del API Gateway para el sistema de gestión de tareas de la empresa XYZ.

## Introducción
El API Gateway actúa como un punto de entrada único para todos los microservicios del sistema. Este componente se encarga de enrutar las solicitudes hacia los microservicios correspondientes.

## Características Principales
- Ruteo de solicitudes: Enruta las solicitudes HTTP a los microservicios gestion_usuario y gestion_tareas.
- Balanceo de carga: Gestiona la distribución de solicitudes entre instancias de microservicios.
- Autenticación y autorización: Valida token JWT para garantizar el acceso seguro.

## Requisitos Previos
- Docker Desktop instalado en el sistema
- Maven instalado para compilar el proyecto o el IDE Spring Tool.
- Java 8
- Git 

## Tecnologías Utilizadas
- Spring Cloud Gateway
- JWT para autenticación

## Endpoints Configurados
|Ruta|Método|Descripción|
|----|------|-----------|
|/api/users/**| Todas | Redirige a los endpoints del microservicio gestion-usuarios|
|/api/tareas/**| Todas | Redirige a los endpoints del microservicio gestion-tareas|
|auth/login| POST | Endpoitn para autenticaciòn y generación de token JWT

## Arquitectura
La arquitectura del sistema es la siguiente:
- El cliente realiza solicitudes al API Gateway
- El API Gateway envía las solicitudes al microservicio correspondiente (gestion-usuarios, gestion-tareas).
- Se realiza autenticación y autorización antes de enrutar la solicitud.

## Configuraciñon application.yml
server:  
&nbsp; port: 9000  
spring:  
&nbsp;   cloud:  
    &nbsp; &nbsp; gateway:      
        &nbsp; &nbsp; &nbsp; routes:  
        &nbsp; &nbsp; &nbsp; &nbsp;         - id: gestor-tareas-route   
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; uri: http://172.17.0.4:8081   
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; predicates:  
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; - Path=/gestor/api/tareas/**  
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; filters:  
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; - name: AddBearerTokenFilter   
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; - id: gestor-usuarios-route   
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; uri: http://172.17.0.3:8082   
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; predicates:   
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; - Path=/gestor/api/usuarios/**   
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; filters:   
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; - name: AddBearerTokenFilter

## Levantar el proyecto en Docker
El Api Gateway está preparado para ejecutarse en un contenedor Docker.

**Requisitos Previos**
- Docker Desktop instalado en el sistema
- Maven instalado para compilar el proyecto o el IDE Spring Tool.
- Java 8
- Git 

**Importante:** El orden de los contenedores debe ser:
- postgres-container
- gestor-usuarios-container
- gestor-tareas-container

---

1. Abrir una terminal (cmd)
2. Clonar el proyecto con el comando:

    git clone https://github.com/d4r1v506/Gateway.git

3. Compilar el proyecto

    (opción 1)

   Si se tiene instalado Maven, ejecutar en una terminal dentro de la raiz del proyecto el comando:
   
   mvn clean install
   
    (opción 2)
    
    Abrir el proyecto con un IDE, de preferencia Spring tool.

   Dar clic derecho en la raiz del proyecto y seleccionar:
    - Run As - Maven Clean
    - Run As - Maven install
    
4. Validar que se creo dentro del directorio target el archivo *gateway.jar*

5. Crear el contenedor de la base postgreSQL "SI AUN NO ESTA CREADO", en la terminal escribimos el comando:

    docker run -d --name postgres_container -e POSTGRES_USER=postgres -e POSTGRES_PASWORD=postgres -p 5432:5432 postgres

    5.1. Ingresamos al contenedor de postgres con el comando:

    docker exec -it postgres_container psql -U postgres

    5.2. Una vez dentro del contenedor usamos el siguiente comando para visualizar las bases de datos: 
    
    \l

    5.3. Si no existen la base de datos *gestion_tareas*  y *gestion_usuarios* ejecutamos los query del archivo: query.sql

    *Nota:* Para conectarse a la base usamos el comando: 
    
    \c gestion_tareas

    \c gestion_usuarios

6. Construir la imagen del microservicio, ingresar a la raiz del proyecto desde la terminal y ejecutar el comando:

    docker build -t gateway:1.0 .

    6.1. Ejecutar el contenedor con el comando:

    docker run -d -p 9000:9000 --name gateway-container gateway:1.0

8. Abrir postman y ejecutar los endpoints de los microservicios gestion-usuario y gestion-tareas con el puerto 9000 

    Ejemplo: http://localhost:9000/gestor/api/usuarios/1719512392


