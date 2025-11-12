# Challenge Backend - API REST con Spring Boot

## 📋 Descripción

API REST desarrollada en Spring Boot que realiza un cálculo matemático con porcentajes dinámicos obtenidos de un servicio externo, implementando mecanismos de resiliencia, caché y registro asíncrono de historial de llamadas al servicio.

## 🚀 Tecnologías Utilizadas

### Core
- **Java 21** - Versión LTS con mejoras de rendimiento y nuevas características del lenguaje
- **Spring Boot 3.5.7** - Framework principal para la construcción de la API REST
- **Maven** - Gestión de dependencias y construcción del proyecto

### Persistencia
- **PostgreSQL** - Base de datos relacional para almacenamiento del historial
- **Spring Data JPA** - Abstracción de acceso a datos

### Resiliencia y Caché
- **Resilience4j** - Implementación de Circuit Breaker para tolerancia a fallos
- **Caffeine Cache** - Caché en memoria de alto rendimiento para almacenar el porcentaje (TTL: 30 minutos)

### Comunicación Externa
- **Spring WebClient** - Cliente HTTP reactivo para consumo de API externa

### Documentación
- **SpringDoc OpenAPI (Swagger)** - Documentación interactiva de la API

### Testing
- **JUnit 5** - Framework de testing unitario
- **Mockito** - Framework de mocking para pruebas
- **Spring Boot Test** - Utilidades de testing integradas

### Infraestructura
- **Docker** - Containerización de la aplicación
- **Docker Compose** - Orquestación de contenedores (API + PostgreSQL)

## 🏗️ Decisiones de Arquitectura

### 1. **Arquitectura en Capas**
```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← Endpoints REST
├─────────────────────────────────────┤
│          Service Layer              │  ← Lógica de negocio
├─────────────────────────────────────┤
│        Repository Layer             │  ← Acceso a datos
├─────────────────────────────────────┤
│      External Service Layer         │  ← Integración con APIs externas
└─────────────────────────────────────┘
```

**Justificación**: Separación clara de responsabilidades, facilita el testing, mantenimiento y escalabilidad.

### 2. **Circuit Breaker con Resilience4j**

**Problema resuelto**: Evitar cascadas de fallos cuando el servicio externo de porcentajes no está disponible.

**Configuración implementada**:
- **Failure Rate Threshold**: 50% (se abre el circuito si 50% de las llamadas fallan)
- **Wait Duration in Open State**: 60 segundos
- **Sliding Window Size**: 10 llamadas para calcular la tasa de fallos
- **Fallback**: Retorno del último valor cacheado

**Ventajas**:
- Protección contra servicios externos lentos o caídos
- Recuperación automática del servicio
- Métricas integradas para monitoreo

### 3. **Caché con Caffeine**

**¿Por qué Caffeine?**
- Alto rendimiento (hasta 5x más rápido que Guava Cache)
- Configuración declarativa con Spring Cache
- Soporte nativo para TTL (Time To Live)
- Eficiente manejo de memoria

**Configuración**:
```java
@Cacheable(value = "percentageCache", key = "'percentage'")
- TTL: 30 minutos
- Almacenamiento: Heap memory
- Eviction Policy: Time-based
```

### 4. **Registro Asíncrono con Interceptor**

**Implementación**:
- **HandlerInterceptor** personalizado para capturar requests/responses
- **@Async** con ThreadPoolTaskExecutor dedicado para escritura en BD
- **WebMvcConfigurer** para registrar el interceptor en URIs específicas

**Ventajas**:
- Cero impacto en el tiempo de respuesta de la API
- Desacoplamiento del registro de la lógica de negocio
- Pool de threads dedicado para operaciones de I/O

**Configuración del Thread Pool**:
```java
- Core Pool Size: 2
- Max Pool Size: 5
- Queue Capacity: 100
```

### 5. **Manejo Centralizado de Excepciones**

**@ControllerAdvice + @ExceptionHandler**:
- Respuestas HTTP consistentes
- Logging centralizado de errores
- Mapeo de excepciones a códigos HTTP apropiados

## 📦 Estructura del Proyecto

```
src/main/java
├── controller/          # Endpoints REST
├── service/            # Lógica de negocio
├── repository/         # Acceso a datos
├── model/              # Entidades JPA
├── dto/                # Data Transfer Objects
├── config/             # Configuraciones (Cache, Async, Circuit Breaker)
├── interceptor/        # Interceptor para historial
├── exception/          # Excepciones personalizadas
└── client/             # Clientes para servicios externos

src/main/resources
├── application.yml     # Configuración de la aplicación
└── application-docker.yml  # Configuración para Docker

docker/
├── Dockerfile          # Imagen de la aplicación
└── docker-compose.yml  # Orquestación de servicios
```

## 🔧 Requisitos Previos

- **Docker** (versión 20.10+)
- **Docker Compose** (versión 2.0+)
- **Java 21** (solo para desarrollo local sin Docker)
- **Maven 3.8+** o **Gradle 8+** (para compilación local)

## 🚀 Instrucciones de Ejecución

### Opción 1: Usando Docker Compose (Recomendado)

1. **Clonar el repositorio**
```bash
git clone <URL_DEL_REPOSITORIO>
cd challenge-backend
```

2. **Levantar los servicios**
```bash
docker-compose up -d
```

3. **Verificar que los contenedores estén corriendo**
```bash
docker-compose ps
```

4. **Acceder a la aplicación**
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- PostgreSQL: localhost:5432

5. **Ver logs**
```bash
docker-compose logs -f api
```

6. **Detener los servicios**
```bash
docker-compose down
```

### Opción 2: Ejecución Local (sin Docker)

1. **Iniciar PostgreSQL localmente**
```bash
# Usando Docker para solo la base de datos
docker run -d \
  --name postgres-challenge \
  -e POSTGRES_DB=challenge_db \
  -e POSTGRES_USER=challenge_user \
  -e POSTGRES_PASSWORD=challenge_pass \
  -p 5432:5432 \
  postgres:15-alpine
```

2. **Compilar el proyecto**
```bash
./mvnw clean package
# o con Gradle
./gradlew build
```

3. **Ejecutar la aplicación**
```bash
./mvnw spring-boot:run
# o con Gradle
./gradlew bootRun
```

### Opción 3: Usando imagen de Docker Hub

```bash
# Descargar la imagen
docker pull <USUARIO_DOCKER_HUB>/challenge-backend:latest

# Ejecutar con docker-compose
docker-compose -f docker-compose-hub.yml up -d
```

## 📝 Endpoints Principales

### 1. Cálculo con Porcentaje Dinámico
```http
POST /api/v1/calculate
Content-Type: application/json

{
  "num1": 100,
  "num2": 50
}

Response:
{
  "result": 165.0,
  "percentage": 10.0,
  "source": "EXTERNAL_API",
  "timestamp": "2025-11-12T10:30:00"
}
```

### 2. Obtener Historial de Llamadas
```http
GET /api/v1/history?page=0&size=10

Response:
{
  "content": [
    {
      "id": 1,
      "timestamp": "2025-11-12T10:30:00",
      "endpoint": "/api/v1/calculate",
      "method": "POST",
      "parameters": "{\"num1\":100,\"num2\":50}",
      "response": "{\"result\":165.0}",
      "statusCode": 200,
      "executionTime": 45
    }
  ],
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0
}
```

### 3. Health Check
```http
GET /actuator/health

Response:
{
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "diskSpace": {"status": "UP"}
  }
}
```

## 🧪 Ejecutar Tests

```bash
# Tests unitarios
./mvnw test

# Tests con cobertura
./mvnw verify

# Ver reporte de cobertura
open target/site/jacoco/index.html
```

## 🔍 Monitoreo y Métricas

### Actuator Endpoints
- Health: `/actuator/health`
- Metrics: `/actuator/metrics`
- Circuit Breaker: `/actuator/circuitbreakers`
- Cache Stats: `/actuator/caches`

### Logs
```bash
# Ver logs en tiempo real
docker-compose logs -f api

# Filtrar por nivel
docker-compose logs api | grep ERROR
```

## ⚙️ Variables de Entorno

```bash
# Base de datos
DB_HOST=postgres
DB_PORT=5432
DB_NAME=challenge_db
DB_USER=challenge_user
DB_PASSWORD=challenge_pass

# API Externa
EXTERNAL_API_URL=https://api.example.com/percentage
EXTERNAL_API_TIMEOUT=5000

# Cache
CACHE_TTL_MINUTES=30

# Circuit Breaker
CB_FAILURE_RATE_THRESHOLD=50
CB_WAIT_DURATION_SECONDS=60
```

## 🐛 Troubleshooting

### La aplicación no inicia
```bash
# Verificar logs
docker-compose logs api

# Verificar que PostgreSQL esté listo
docker-compose exec postgres pg_isready
```

### El servicio externo falla constantemente
- El Circuit Breaker se abrirá automáticamente
- Se utilizará el valor cacheado
- Revisar configuración en `application.yml`

### Base de datos no conecta
```bash
# Verificar conectividad
docker-compose exec api ping postgres

# Verificar credenciales
docker-compose exec postgres psql -U challenge_user -d challenge_db
```

## 📚 Documentación Adicional

- [Swagger UI](http://localhost:8080/swagger-ui.html) - Documentación interactiva
- [Postman Collection](docs/postman-collection.json) - Colección de requests
- [Architecture Decision Records](docs/ADR.md) - Decisiones de arquitectura detalladas

## 👥 Contribución

1. Fork el proyecto
2. Crear rama de feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## ✅ Checklist Completado

- [x] Cálculo con porcentaje dinámico desde API externa
- [x] Circuit Breaker con Resilience4j
- [x] Caché de porcentaje (30 minutos) con Caffeine
- [x] Historial de llamadas con paginación
- [x] Registro asíncrono con interceptor
- [x] PostgreSQL en Docker
- [x] docker-compose.yml funcional
- [x] Documentación con Swagger
- [x] Tests unitarios con JUnit y Mockito
- [x] README con instrucciones claras
- [x] Manejo de errores centralizado
- [x] Arquitectura en capas limpia

## 📧 Contacto

Para preguntas o sugerencias, contactar a: [tu-email@example.com]

---

**Desarrollado con ☕ y Spring Boot**