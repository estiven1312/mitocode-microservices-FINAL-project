# Auth Service

Microservicio de autenticacion y autorizacion para emitir JWT, refrescar sesiones y administrar usuarios/roles.

## Objetivo en la arquitectura

`auth-service` centraliza identidad y permisos para otros microservicios (por ejemplo, `contract-service`).
La autorizacion se basa en authorities dentro del token JWT.

## Roles de negocio (integrados con contratos)

Los roles se inicializan en `db/data/init.sql` y representan acciones funcionales del dominio de contratos:

- `CREATE_CONTRACT`
- `APPROVE_CONTRACT`
- `SIGN_CONTRACT`
- `CREATE_USERS`

Estos mismos valores viajan en el claim `roles` del access token generado por `JwtService`.

## Casos de uso

### 1) Login y obtencion de tokens

- Endpoint: `POST /api/v1/auth/login`
- Entrada: usuario/password
- Salida: `accessToken` + `refreshToken`
- Uso en contratos: el `accessToken` incluye `roles` y permite autorizar operaciones de contratos sin consultar BD en cada request.

### 2) Refresh de sesion

- Endpoint: `POST /api/v1/auth/refresh`
- Entrada: `refreshToken`
- Salida: nuevo `accessToken` (y nuevo refresh segun implementacion actual)
- Uso en contratos: mantiene sesiones activas en front o gateway sin reautenticacion completa.

### 3) Validacion de token

- Endpoint: `POST /api/v1/auth/validate`
- Entrada: token JWT
- Salida: estado de validez/expiracion
- Uso en contratos: util para validacion remota desde API Gateway o servicios legacy.

### 4) Registro de usuarios con roles

- Endpoint: `POST /api/v1/users/register`
- Seguridad: `@PreAuthorize("hasAuthority('CREATE_USERS')")`
- Uso en contratos: permite crear usuarios operativos y asignarles capacidades de negocio (`CREATE_CONTRACT`, `APPROVE_CONTRACT`, `SIGN_CONTRACT`).

### 5) Bootstrap admin en desarrollo

- Componente: `DevelopAdminLoader`
- Perfil activo: `dev`
- Comportamiento: si no existe el usuario admin configurado (`auth.bootstrap.username`), lo crea con todos los roles disponibles en BD.

## Integracion recomendada con `contract-service`

### Opcion A: autorizacion local por claim `roles` (mas eficiente)

1. `contract-service` valida firma/expiracion del JWT.
2. Convierte claim `roles` en authorities de Spring Security.
3. Protege endpoints con anotaciones:
   - `@PreAuthorize("hasAuthority('CREATE_CONTRACT')")`
   - `@PreAuthorize("hasAuthority('APPROVE_CONTRACT')")`
   - `@PreAuthorize("hasAuthority('SIGN_CONTRACT')")`

### Opcion B: validacion remota

`contract-service` (o gateway) consulta `POST /api/v1/auth/validate` cuando requiera validacion centralizada.

## Modelo de datos inicial (PostgreSQL)

`db/data/init.sql` crea:

- `roles`
- `users`
- `users_roles` (muchos a muchos)

Y precarga los roles de negocio. Este archivo se monta en Docker como script de inicializacion:

- `./db/data/init.sql:/docker-entrypoint-initdb.d/init.sql:ro`

## API

Base path: `/api/v1`

- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/validate`
- `POST /users/register` (requiere `CREATE_USERS`)

Swagger UI:

- `http://localhost:8081/api/v1/swagger-ui.html`

## Ejecucion rapida

```bash
./gradlew clean test
./gradlew bootRun
```

Con Docker (BD):

```bash
docker compose up --build
```

## Variables de entorno relevantes

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_ACCESS_EXP_SECONDS`
- `JWT_REFRESH_EXP_SECONDS`
- `AUTH_BOOTSTRAP_USERNAME` (si se usa config por env)
- `AUTH_BOOTSTRAP_PASSWORD` (si se usa config por env)
