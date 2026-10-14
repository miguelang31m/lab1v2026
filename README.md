# Lab 1 + Lab 2 — Banco / CI-CD

Aplicación bancaria del Laboratorio 1 evolucionada para el Laboratorio 2 de Arquitectura de Software.

## Funcionalidades

- Consultar, crear, actualizar y eliminar clientes.
- Realizar transferencias entre cuentas.
- Consultar histórico de transacciones por cuenta.
- Health check y versión del backend.
- Pruebas unitarias con JUnit 5 + Mockito.
- Cobertura con JaCoCo.
- Pipeline CI/CD con GitHub Actions.
- Análisis de calidad opcional con SonarCloud.
- Escaneo de seguridad opcional con Snyk.
- Construcción del JAR.
- Construcción y publicación opcional de imagen Docker.
- Despliegue automático a Render mediante Deploy Hook.

## Endpoints principales

- `GET /api/customers`
- `POST /api/customers`
- `PUT /api/customers/{id}`
- `DELETE /api/customers/{id}`
- `GET /api/transactions`
- `GET /api/transactions/account/{accountNumber}`
- `POST /api/transactions/transfer`
- `GET /api/system/health`
- `GET /api/system/version`

## Ejecución local

### Base de datos

```sql
CREATE DATABASE IF NOT EXISTS banco2026;
CREATE USER IF NOT EXISTS 'banco_user'@'localhost' IDENTIFIED BY 'banco123';
GRANT ALL PRIVILEGES ON banco2026.* TO 'banco_user'@'localhost';
FLUSH PRIVILEGES;
```

### Backend

```bash
./mvnw spring-boot:run
```

Windows:

```bat
mvnw.cmd spring-boot:run
```

Backend: `http://localhost:8080`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend: `http://localhost:5173`

## Pruebas y cobertura

```bash
./mvnw clean verify
```

Reporte JaCoCo:

`target/site/jacoco/index.html`

## Docker

```bash
docker build -t lab1v2026 .
docker run --rm -p 8080:8080 -e SPRING_PROFILES_ACTIVE=cloud lab1v2026
```

Luego abrir:

`http://localhost:8080/api/system/health`

## Pipeline

Workflow: `.github/workflows/build.yml`

Se ejecuta en:

- `push` a `main`.
- `push` a `feature/lab2-cicd`.
- `pull_request` hacia `main`.
- ejecución manual (`workflow_dispatch`).

Jobs:

`tests -> sonar/security -> build -> docker -> deploy`

Los jobs de SonarCloud, Snyk, Docker Hub y Render se omiten de forma segura mientras sus secrets no estén configurados.

## Secrets de GitHub

Ver `SECRETS_GITHUB.md`.

## Despliegue a Render

Ver `RENDER_PASO_A_PASO.md`.

## Entrega Lab 2

Ver `LAB2_PASO_A_PASO.md` e `INFORME_LAB2_BORRADOR.md`.
