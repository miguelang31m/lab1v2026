# Laboratorio 2 — Entorno colaborativo de integración y despliegue continuo

## Introducción

En este laboratorio se evolucionó la aplicación bancaria desarrollada previamente en Spring Boot, incorporando prácticas de integración y despliegue continuo. El objetivo fue automatizar la validación, construcción, análisis de calidad, análisis de seguridad y despliegue del backend mediante un pipeline ejecutado desde GitHub Actions.

La solución integra pruebas unitarias con JUnit y Mockito, cobertura mediante JaCoCo, análisis estático con SonarCloud, análisis de vulnerabilidades con Snyk, construcción del artefacto JAR, generación de una imagen Docker y despliegue del backend en un proveedor de nube.

## Objetivo general

Implementar un entorno colaborativo de integración y despliegue continuo para la aplicación bancaria, automatizando pruebas, análisis de calidad, construcción y despliegue mediante GitHub Actions.

## Objetivos específicos

- Configurar un pipeline CI/CD en GitHub Actions.
- Ejecutar pruebas unitarias automáticamente ante cambios en el repositorio.
- Medir cobertura de código con JaCoCo.
- Analizar calidad del código con SonarCloud.
- Ejecutar análisis de seguridad con Snyk.
- Generar automáticamente el JAR de la aplicación.
- Empaquetar el backend en una imagen Docker.
- Desplegar el backend en un proveedor cloud.
- Aplicar una estrategia trunk-based mediante una rama de corta duración y Pull Request hacia `main`.

## Herramientas empleadas

- Java 17
- Spring Boot
- Maven
- JUnit 5
- Mockito
- JaCoCo
- Git y GitHub
- GitHub Actions
- SonarCloud
- Snyk
- Docker
- Render
- MySQL para ejecución local
- H2 para demostración cloud rápida o MySQL administrado si se configura externamente

## Arquitectura CI/CD propuesta

```text
Desarrollador
    |
    v
feature/lab2-cicd
    |
    v
Pull Request -> main
    |
    v
GitHub Actions
    |
    +--> Tests JUnit/Mockito
    |
    +--> JaCoCo
    |
    +--> SonarCloud
    |
    +--> Snyk
    |
    +--> Build JAR
    |
    +--> Docker
    |
    v
Render
    |
    v
Backend público
```

## Procedimiento

### 1. Preparación del proyecto

Se tomó como base el backend funcional desarrollado en el Laboratorio 1. Se mantuvo la arquitectura por capas y se añadieron los componentes necesarios para automatizar su validación y despliegue.

**[CAPTURA: estructura del proyecto]**

### 2. Estrategia de ramas

Se creó la rama `feature/lab2-cicd` a partir de `main`. Los cambios del laboratorio se subieron a esta rama y posteriormente se abrió un Pull Request hacia la rama principal.

**[CAPTURA: rama feature/lab2-cicd]**
**[CAPTURA: Pull Request]**

### 3. Pruebas unitarias

Se implementaron pruebas sobre la lógica de clientes y transferencias utilizando JUnit 5 y Mockito. Las pruebas verifican escenarios exitosos y escenarios de error, como cuentas inexistentes, saldo insuficiente y cuentas duplicadas.

**[CAPTURA: pruebas exitosas]**

### 4. Cobertura con JaCoCo

JaCoCo fue agregado al ciclo de Maven. El comando `mvn clean verify` ejecuta las pruebas y genera el reporte de cobertura en `target/site/jacoco`.

**[CAPTURA: reporte JaCoCo]**

### 5. Pipeline en GitHub Actions

Se creó `.github/workflows/build.yml`. El workflow se ejecuta ante push a `main`, push a `feature/lab2-cicd`, Pull Request hacia `main` o ejecución manual.

Los jobs configurados son pruebas y cobertura, SonarCloud, Snyk, construcción del JAR, Docker y despliegue.

**[CAPTURA: GitHub Actions]**

### 6. SonarCloud

Se vinculó el repositorio con SonarCloud y se configuraron los secrets necesarios en GitHub. El análisis se ejecuta desde el pipeline después de que las pruebas terminan correctamente.

**[CAPTURA: SonarCloud Quality Gate]**

### 7. Snyk

Se agregó un análisis de seguridad para verificar vulnerabilidades conocidas en las dependencias Maven.

**[CAPTURA: Snyk]**

### 8. Construcción del artefacto

El pipeline genera el archivo `lab1v2026-0.0.1-SNAPSHOT.jar` y lo publica como artefacto descargable dentro de la ejecución de GitHub Actions.

**[CAPTURA: artefacto JAR]**

### 9. Docker

Se agregó un Dockerfile multietapa. La primera etapa compila la aplicación y la segunda utiliza Java 17 JRE para ejecutar únicamente el JAR resultante.

**[CAPTURA: job Docker]**

### 10. Despliegue en nube

Se creó un servicio web en Render utilizando el Dockerfile del repositorio. El workflow de GitHub Actions contiene un job `deploy` que utiliza un Deploy Hook almacenado como secret para solicitar un nuevo despliegue después de integrar cambios en `main`.

Para la demostración rápida puede utilizarse el perfil `cloud`, que usa H2 dentro del contenedor. Para un escenario persistente se pueden proporcionar credenciales de una instancia MySQL mediante variables de entorno.

**[CAPTURA: servicio Render activo]**
**[CAPTURA: /api/system/health]**
**[CAPTURA: /api/customers]**

### 11. Merge a main

Una vez verificadas las validaciones automáticas del Pull Request, los cambios fueron integrados a `main`. Esto disparó nuevamente el pipeline y ejecutó el despliegue.

**[CAPTURA: Pull Request merged]**

## Resultados

Se obtuvo un flujo automatizado en el cual cada cambio relevante es validado por pruebas unitarias, cobertura, análisis de calidad y seguridad antes de generar el artefacto y preparar su despliegue. El uso de ramas cortas y Pull Requests permitió demostrar un flujo trunk-based alrededor de la rama principal.

## Conclusiones

La automatización del proceso permitió reducir las verificaciones manuales necesarias antes de integrar cambios al proyecto. GitHub Actions centralizó las pruebas y la construcción del backend, mientras que JaCoCo, SonarCloud y Snyk aportaron visibilidad sobre cobertura, mantenibilidad y seguridad.

El uso de Docker permitió estandarizar el entorno de ejecución y simplificar el despliegue en la nube. Finalmente, el flujo mediante Pull Request permitió aplicar una estrategia colaborativa en la que los cambios se validan antes de incorporarse a la rama principal.

## Repositorio

`https://github.com/miguelang31m/lab1v2026`

## URL del backend en nube

`[PEGAR AQUÍ LA URL DE RENDER]`
