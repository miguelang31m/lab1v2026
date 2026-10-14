# Secrets de GitHub para el Lab 2

Ruta: **GitHub > repositorio > Settings > Secrets and variables > Actions > New repository secret**.

## SonarCloud

- `SONAR_TOKEN`: token generado en SonarCloud.
- `SONAR_PROJECT_KEY`: Project Key mostrado por SonarCloud.
- `SONAR_ORGANIZATION`: identificador de la organización en SonarCloud.

## Snyk

- `SNYK_TOKEN`: token personal de Snyk.

## Docker Hub (opcional, pero deja evidencia adicional)

- `DOCKERHUB_USERNAME`: usuario de Docker Hub.
- `DOCKERHUB_TOKEN`: access token de Docker Hub. No usar la contraseña normal.

## Render

- `RENDER_DEPLOY_HOOK_URL`: URL del Deploy Hook del servicio Render.

## Importante

Nunca guardar tokens o contraseñas directamente dentro de `build.yml`, `pom.xml` o el repositorio.
