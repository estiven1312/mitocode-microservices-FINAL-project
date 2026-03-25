# E-Signature Microservice

Este microservicio se encarga de **firmar contratos** y guardar el resultado de la firma.

## Endpoint principal

`POST http://localhost:8090/signature/contract/{contractId}`

Ejemplo real:

`POST http://localhost:8090/signature/contract/c88c7ed2-6112-4c88-9d2a-ffc103599761`

## Que valida antes de procesar

Antes de ejecutar la firma, el servicio valida lo siguiente:

1. Que el request tenga token JWT en `Authorization: Bearer <token>`.
2. Que el token sea valido (firma y vigencia).
3. Que el usuario tenga el permiso `SIGN_CONTRACT`.
4. Que el `contractId` tenga formato UUID correcto.
5. Que el body venga con los datos necesarios para la firma.

Si algo falla, responde con errores claros (`401`, `403`, `400`, etc.).

## Body esperado

```json
{
  "urlDocument": "https://storage/contrato.pdf",
  "email": "notificaciones@empresa.com",
  "signerName": "Juan",
  "signerLastName": "Perez",
  "signerDni": "12345678",
  "signerPhone": "999888777",
  "signerEmail": "juan.perez@correo.com"
}
```

## Flujo funcional (simple)

1. Llega la solicitud de firma con `contractId` + datos del firmante.
2. Se valida seguridad (token y permiso).
3. Se valida que el contrato exista y este en estado permitido para firmar.
4. Se ejecuta la firma del documento.
5. Se actualiza el estado del contrato como firmado.
6. Se publica el evento de contrato firmado para otros sistemas.
7. Se persiste el resultado de firma en base de datos.

Si un paso falla, se activa compensacion para dejar el proceso consistente.

## Que sigue despues de llamar este endpoint

- Si todo sale bien, retorna `202 Accepted`.
- La firma queda en proceso/completada segun el flujo interno.
- Puedes consultar el resultado con:
  - `GET http://localhost:8090/signature/contracts/{contractId}`

## Respuestas frecuentes

- `202 Accepted`: solicitud aceptada para firmar.
- `400 Bad Request`: datos invalidos (por ejemplo UUID mal formado).
- `401 Unauthorized`: token invalido o expirado.
- `403 Forbidden`: token valido pero sin permiso `SIGN_CONTRACT`.
- `404 Not Found`: no existe firma para el contrato consultado.
- `500 Internal Server Error`: error inesperado durante la firma.

## Ejemplo rapido con curl

```bash
curl -X POST "http://localhost:8090/signature/contract/c88c7ed2-6112-4c88-9d2a-ffc103599761" \
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

