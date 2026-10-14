# Lab 2 — Paso a paso de entrega

## Estado entregado en este ZIP

Ya está preparado:

- JaCoCo en `pom.xml`.
- Pruebas JUnit/Mockito.
- Workflow `.github/workflows/build.yml`.
- Jobs de tests, SonarCloud, Snyk, build, Docker y deploy.
- Dockerfile.
- Variables de entorno para local/nube.
- Perfil `cloud` para demostración rápida.
- Health check y versión.
- Frontend configurable por `VITE_API_URL`.
- Documentación para GitHub Secrets y Render.

## Lo que debe hacer el estudiante

### A. Crear la rama trunk-based

Desde el repo actualizado:

```bash
git checkout main
git pull origin main
git checkout -b feature/lab2-cicd
```

Copiar/reemplazar los archivos de este ZIP dentro de su repo y luego:

```bash
git add .
git commit -m "feat: agregar pipeline CI/CD laboratorio 2"
git push -u origin feature/lab2-cicd
```

**Captura 1:** rama `feature/lab2-cicd` visible en GitHub.

### B. Crear Pull Request

En GitHub crear PR:

`feature/lab2-cicd -> main`

No hacer merge todavía.

**Captura 2:** Pull Request abierto.

### C. Verificar GitHub Actions

Ir a **Actions > CI/CD Pipeline**.

Debe pasar al menos `Unit tests + JaCoCo` y `Build JAR`. Los servicios sin secrets se omiten sin dañar el pipeline.

**Captura 3:** workflow con tests exitosos.

### D. SonarCloud

1. Iniciar sesión con GitHub en SonarCloud.
2. Importar el repo.
3. Desactivar Automatic Analysis si el análisis se hará desde GitHub Actions.
4. Obtener Project Key y Organization.
5. Crear token.
6. En GitHub agregar secrets:
   - `SONAR_TOKEN`
   - `SONAR_PROJECT_KEY`
   - `SONAR_ORGANIZATION`
7. Reejecutar workflow.

**Captura 4:** job SonarCloud exitoso.
**Captura 5:** panel SonarCloud mostrando Quality Gate/cobertura.

### E. Snyk

1. Entrar a Snyk con GitHub.
2. Generar/copiar token.
3. Crear secret `SNYK_TOKEN`.
4. Reejecutar workflow.

**Captura 6:** job Snyk security scan.

### F. Render

Seguir `RENDER_PASO_A_PASO.md`.

Agregar secret:

`RENDER_DEPLOY_HOOK_URL`

### G. Merge a main

Cuando el PR tenga pipeline correcto:

1. Merge Pull Request.
2. El push a `main` dispara el pipeline otra vez.
3. El job `deploy` llama a Render.

**Captura 7:** PR merged.
**Captura 8:** pipeline de `main` completo.
**Captura 9:** backend funcionando en URL pública.

## Evidencia mínima recomendada

- Rama adicional.
- PR abierto.
- Tests en GitHub Actions.
- Reporte/resultado JaCoCo.
- SonarCloud.
- Snyk.
- Build JAR.
- Docker image job.
- Deploy Render.
- PR merged a main.
- Backend público respondiendo health y customers.
