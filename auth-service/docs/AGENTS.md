# Proyecto Auth Service

## Descripción

Para este proyecto debe tenerse en cuenta los siguientes objetivos:

- Crear un servicio de autenticación utilizando Spring Boot.
- Este servicio es únicamente para la autenticación, generación de token, generación de refresh token, validación de
  token.
- Se tiene el módulo de usuarios, el cual permite el registro de usuarios con un rol en específico.
- Los roles son: CREATE_CONTRACT, APPROVE_CONTRACT, SIGN_CONTRACT, CREATE_USERS.
- Implementar JWT (JSON Web Tokens) para la gestión de tokens de autenticación.

## Requisitos

- El servicio debe exponer una API REST para las operaciones de autenticación y gestión de usuarios.
- El servicio debe ser capaz de generar y validar JWTs para la autenticación de usuarios.
- El servicio debe permitir el registro de usuarios con roles específicos.
- La api de registro de usuarios debe ser protegida y solo accesible para el rol CREATE_USERS.
- El servicio debe ser capaz de manejar la expiración de tokens y la generación de refresh tokens.
- El secret key y el tiempo de expiración del token deben ser configurables a través de variables de entorno o archivos
  de configuración.
- Se debe crear las tablas en BD para almacenar los usuarios y roles. Un usuario puede tener varios roles y un rol puede
  ser asignado a varios usuarios (relación muchos a muchos).
- El servicio debe ser capaz de manejar errores de autenticación y proporcionar respuestas adecuadas (Usa especificación
  de problem_details y un controller advice).
- Crea errores de dominio para lanzar excepciones (UsuarioExistente, UsuarioNoEncontrado, CredencialesNoValidas,
  RoleNotFOund) hazlo todo en ingles y con camel case adecuado.
- Debes documentar el API utilizando OpenAPI/Swagger para facilitar su uso por parte de otros desarrolladores.

## Salidas esperadas

- Un proyecto Spring Boot con la implementación del servicio de autenticación.
- Endpoints para el registro de usuarios, autenticación, generación de tokens y validación de
- API REST documentada con OpenAPI/Swagger.
- Código limpio y bien estructurado siguiendo las mejores prácticas de desarrollo de software.
- Seguir las capas de CONTROLLER -> SERVICE -> REPOSITORY -> DOMAIN
- Implementar pruebas unitarias para los servicios.
- La configuracion centralizada esta en
    ```
    config:
    import: "optional:configserver:http://localhost:8888"
  ```
- El servicio de autenticación se conecta a una base de datos (POSTGRESQL) debes crear un docker-compose para ello.
- Crear un DOCKERFILE para el servicio de autenticación y un docker-compose para levantar el servicio junto con la base
  de datos. Usa como base la imagen oficial de OpenJDK 17.

```
FROM eclipse-temurin:21-jdk-alpine AS compiler
WORKDIR /app

COPY gradlew ./
COPY gradlew.bat ./
COPY gradle ./gradle
COPY settings.gradle.kts ./
COPY build.gradle.kts ./
COPY src ./src

RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test --no-daemon

FROM eclipse-temurin:21-jdk-alpine AS extractor
WORKDIR /layers
COPY --from=compiler /app/build/libs/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=extractor /layers/dependencies/ ./
COPY --from=extractor /layers/snapshot-dependencies/ ./
COPY --from=extractor /layers/spring-boot-loader/ ./
COPY --from=extractor /layers/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

```