# Despliegue rápido del backend en Render

Este procedimiento está pensado para minimizar el tiempo manual del Lab 2.

## 1. Crear Web Service

1. Entrar a Render con GitHub.
2. Crear **New > Web Service**.
3. Seleccionar el repo `miguelang31m/lab1v2026`.
4. Runtime: **Docker**.
5. Dockerfile: `./Dockerfile`.
6. Branch inicial: `main`.

## 2. Variables de entorno

Para la demostración más rápida agregue:

- `SPRING_PROFILES_ACTIVE=cloud`
- `APP_VERSION=2.0.0`

El perfil `cloud` usa H2 temporal para que el backend arranque sin contratar una base MySQL externa.

### Opción MySQL real en nube

Si el profesor exige MySQL también en nube, NO defina `SPRING_PROFILES_ACTIVE=cloud` y configure:

- `DB_URL=jdbc:mysql://HOST:PUERTO/banco2026?useSSL=true&serverTimezone=UTC`
- `DB_USERNAME=...`
- `DB_PASSWORD=...`
- `DDL_AUTO=update`

## 3. Health check

Configurar como ruta de salud:

`/api/system/health`

## 4. Deploy Hook

En Settings del servicio Render busque **Deploy Hook**, copie la URL y créela como secret de GitHub:

`RENDER_DEPLOY_HOOK_URL`

El job `deploy` de `.github/workflows/build.yml` llama este hook únicamente cuando el pipeline corre sobre `main`.

## 5. Pruebas para la evidencia

Abrir:

- `https://TU-SERVICIO.onrender.com/api/system/health`
- `https://TU-SERVICIO.onrender.com/api/system/version`
- `https://TU-SERVICIO.onrender.com/api/customers`

Debe tomar capturas de:

1. Servicio Render en estado activo.
2. URL `/api/system/health` respondiendo `UP`.
3. URL `/api/customers` respondiendo desde nube.
4. GitHub Actions con el job Deploy exitoso.
