# Proyecto de MICROSERVICIOS MITOCODE

## Descripción

El proyecto es una arquitectura de microservicios Spring Boot (Java 21) para la gestión de contratos y firmas electrónicas.

## Puertos de Servicios

Al arrancar todas las apps, usar el perfil dev

| Servicio | Puerto |
|----------|--------|
| contracts-microservice | 8088 |
| e-signature-microservice | 8090 |
| signature-provider-service | 9095 |
| accountability-microservice | 8092 |
| auth-service | 8093 |
| discovery-server (Eureka) | 8761 |
| config-server | 8888 |

## Infraestructura (Docker)

| Componente | Puerto |
|------------|--------|
| PostgreSQL (contracts) | 5433 |
| PostgreSQL (auth) | 5435 |
| PostgreSQL (accountability) | 5432 |
| MongoDB | 27017 |
| Kafka | 9092 |
| Kafka UI | 6048 |
| OTEL Collector | 4317, 4318 |

## Arquitectura de Flujo

```mermaid
sequenceDiagram
    participant USUARIO
    participant AUTH as auth-service<br/>(8093)
    participant CONTRACTS as contracts-microservice<br/>(8088)
    participant ACCOUNT as accountability-microservice<br/>(8092)
    participant ESIGN as e-signature-microservice<br/>(8090)
    participant SIGPROVIDER as signature-provider-service<br/>(9095)
    participant KAFKA as Kafka
    participant MONGODB as MongoDB<br/>(27017)

    Note over USUARIO,AUTH: 1. Autenticación
    USUARIO->>AUTH: Login (user/pass)
    AUTH-->>USUARIO: JWT Token

    Note over USUARIO,CONTRACTS: 2. Crear Contrato
    USUARIO->>CONTRACTS: POST /api/v1/contracts (JWT)
    CONTRACTS-->>USUARIO: Contract Created

    Note over CONTRACTS,ACCOUNT: 3. Validar Pago
    CONTRACTS->>ACCOUNT: Validar código pago
    ACCOUNT-->>CONTRACTS: Pago válido

    Note over USUARIO,ESIGN: 4. Solicitar Firma
    USUARIO->>ESIGN: POST /signature/contract/{id} (JWT)
    ESIGN->>ESIGN: Validar Token localmente (clave en config)

    Note over ESIGN,CONTRACTS: 5. Validar Estado Contrato
    ESIGN->>CONTRACTS: GET /api/v1/contracts/{id}
    CONTRACTS-->>ESIGN: Contract Status

    Note over ESIGN,SIGPROVIDER: 6. Solicitar Firma al Provider
    ESIGN->>SIGPROVIDER: POST /docu-sign
    SIGPROVIDER-->>ESIGN: hash + urlSignedFile

    Note over ESIGN,MONGODB: 7. Guardar Firma
    ESIGN->>MONGODB: Save Signature

    Note over ESIGN,KAFKA: 8. Publicar Evento
    ESIGN->>KAFKA: Publish ContractSignedEvent

    Note over KAFKA,CONTRACTS: 9. Consumir Evento
    KAFKA->>CONTRACTS: ContractSignedEvent
    CONTRACTS->>CONTRACTS: Update Status + Hash
```

## Microservicios

### contracts-microservice (8088)
- CRUD de contratos
- Gestión de estados (State Machine)
- Historial de aprobaciones
- Consumer de eventos de firma (Kafka)
- Integración con accountability para pagos

### e-signature-microservice (8090)
- Orquestador de firma (Saga Pattern)
- Valida estado del contrato
- Llama a signature-provider-service
- Persiste firmas en MongoDB
- Publica eventos de firma a Kafka

### signature-provider-service (9095)
- Simula proveedor de firma electrónica
- Genera hash de documento
- Genera URL de documento firmado
- **NO registrado en Eureka ni usa Kafka**

### accountability-microservice (8092)
- Gestión de pagos
- Validación de códigos de pago
- Integración con contracts

### auth-service (8093)
- Autenticación y autorización JWT
- Gestión de usuarios y roles
- **OpenAPI/Swagger** disponible (único microservicio con documentación API)

## Estados de Contrato

```mermaid
stateDiagram-v2
    [*] --> IN_DRAFT
    IN_DRAFT --> IN_REVIEW: SUBMIT_FOR_REVIEW
    IN_REVIEW --> PENDING_APPROVAL_BY_MANAGER: SEND_FOR_MANAGER_APPROVAL
    PENDING_APPROVAL_BY_MANAGER --> APPROVED_BY_MANAGER: APPROVE_BY_MANAGER
    APPROVED_BY_MANAGER --> SENT_TO_EXTERNAL: SEND_TO_EXTERNAL
    SENT_TO_EXTERNAL --> APPROVED_BY_EXTERNAL: VALIDATE_BY_EXTERNAL
    APPROVED_BY_EXTERNAL --> SENT_TO_LEGAL: SEND_TO_LEGAL
    SENT_TO_LEGAL --> APPROVED_BY_LEGAL: VALIDATE_BY_LEGAL
    APPROVED_BY_LEGAL --> SIGNED: SIGN_CONTRACT
    SIGNED --> ACTIVE: ACTIVATE
    ACTIVE --> FINISHED: FINISH
```

## Tecnologías

| Categoría | Tecnología |
|-----------|------------|
| Framework | Spring Boot 4.0.3 |
| Lenguaje | Java 21 |
| Mensajeria | Apache Kafka |
| Base de Datos | PostgreSQL, MongoDB |
| Registry | Netflix Eureka |
| Config | Spring Cloud Config |
| Resilience | Resilience4j (Circuit Breaker, Retry) |
| Seguridad | JWT, OAuth2 Resource Server |
| Observabilidad | OpenTelemetry |

## Ejemplo: Crear Contrato

```bash
curl -X POST "http://localhost:8088/api/v1/contracts" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Contrato de Servicio TI",
    "description": "Soporte y mantenimiento",
    "type": "CONTRACT",
    "serviceType": "SERVICE",
    "startDate": "2026-03-10",
    "endDate": "2026-12-31",
    "thirdPartyId": "uuid-tercero",
    "createdBy": "uuid-usuario",
    "requestedArea": "uuid-area",
    "requestedCompany": "uuid-empresa",
    "amount": 15000.50,
    "urlFile": "http://ejemplo.com/contrato.pdf"
  }'
```

## Ejemplo: Firmar Contrato

```bash
curl -X POST "http://localhost:8090/signature/contract/{contractId}" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "urlDocument": "https://storage/contrato.pdf",
    "email": "notificaciones@empresa.com",
    "signerName": "Juan",
    "signerLastName": "Perez",
    "signerDni": "12345678",
    "signerPhone": "999888777",
    "signerEmail": "juan.perez@correo.com"
  }'
```

## Ejemplo: Login

```bash
curl -X POST "http://localhost:8093/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario",
    "password": "contraseña"
  }'
```

---

## Checklist de Entrega

### Requisitos Generales

- [x] **Caso de negocio**: Proceso de firma de contratos con múltiples pasos
- [x] **SAGA**: Orquestación en e-signature-microservice (4 pasos: Validar → Firmar → Publicar → Persistir)
- [x] **Compensaciones**: Cada paso del SAGA tiene método `compensate()` para revertir
- [x] **RestClient**: Comunicación HTTP entre servicios via `RestClient`
- [x] **@CircuitBreaker**: Implementado en `SignatureProviderClient` y `AccountabilityPortAdapter`
- [x] **@Retry**: Implementado en `SignatureProviderClient` y `AccountabilityPortAdapter`
- [x] **Eureka**: `discovery-server` + clientes en cada microservicio
- [x] **Config Server**: config-server disponible en puerto 8888
- [x] **Bases de datos independientes**:
  - contracts-microservice → PostgreSQL (5433)
  - auth-service → PostgreSQL (5435)
  - accountability-microservice → PostgreSQL (5432)
  - e-signature-microservice → MongoDB (27017)
- [x] **Kafka Producer**: e-signature-microservice publica eventos
- [x] **Kafka Consumer**: contracts-microservice consume eventos

### Seguridad

- [x] **JWT**: auth-service genera tokens JWT
- [x] **Validación de token**: Cada microservicio valida JWT localmente (clave en config)
- [x] **@PreAuthorize**: Implementado en endpoints de contratos y firma

### Documentación y Observabilidad

- [x] **Swagger**: auth-service documentado con OpenAPI
- [ ] **API Gateway**: No implementado (opcional)
- [ ] **Grafana/Prometheus**: No implementado (opcional)

### Infraestructura

- [x] **docker-compose.yml**: PostgreSQL, MongoDB, Kafka, Kafka UI, OTEL Collector
- [x] **Ejecución**: `docker compose up -d` levanta toda la infraestructura

### Documentación

- [x] **README.md**: Este archivo con flujo de negocio y arquitectura
