# Lab 1 2026 - Banco

Proyecto completo del laboratorio de Arquitectura de Software.

## 1. Crear la base de datos
En MySQL:

```sql
CREATE DATABASE banco2026;
```

Por defecto el backend usa:
- usuario: root
- contraseña: root

Si tu MySQL usa otra contraseña, cambia `src/main/resources/application.properties`.

## 2. Ejecutar backend
Windows:

```bat
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Backend: http://localhost:8080

## 3. Ejecutar frontend
En otra terminal:

```bash
cd frontend
npm install
npm run dev
```

Frontend: http://localhost:5173

## Funciones
- Consultar y crear clientes.
- Eliminar clientes (opcional del taller).
- Realizar transferencias entre cuentas.
- Consultar el histórico por cliente.

## Endpoints principales
- GET /api/customers
- POST /api/customers
- PUT /api/customers/{id}
- DELETE /api/customers/{id}
- GET /api/transactions
- GET /api/transactions/account/{accountNumber}
- POST /api/transactions/transfer
