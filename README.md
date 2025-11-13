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

### 1. **Arquitectura Hexagonal (Ports & Adapters)**
```
┌─────────────────────────────────────────────────┐
│       Infrastructure Layer (Adapters)            │
│  ┌──────────────────┐        ┌────────────────┐ │
│  │  In Adapters     │        │  Out Adapters  │ │
│  ├─ Controllers     │        ├─ Repositories  │ │
│  ├─ Interceptor     │        ├─ External APIs │ │
│  └─ Config/Filter   │        └────────────────┘ │
└──────────────────┬──────────────────────────────┘
                   │
        ┌──────────┴──────────┐
        │  Application Layer   │
        │  (Use Cases/Ports)   │
        └──────────┬──────────┘
                   │
        ┌──────────┴──────────┐
        │   Domain Layer       │
        │  (Models/Records)    │
        └──────────────────────┘
```

**Justificación**: 
- Separación clara entre puertos (interfaces) y adaptadores (implementaciones)
- Independencia de frameworks y detalles técnicos
- Facilita testing y cambios futuros en infraestructura

### 2. **Circuit Breaker con Resilience4j**

**Problema resuelto**: Evitar cascadas de fallos cuando el servicio externo de porcentajes (https://csrng.net) no está disponible.

**Configuración implementada**:
- **Failure Rate Threshold**: 50% (se abre el circuito si 50% de las llamadas fallan)
- **Sliding Window Size**: 5 llamadas para calcular la tasa de fallos
- **Minimum Calls**: 3 llamadas antes de evaluar la tasa de error
- **Wait Duration in Open State**: 30 segundos
- **Fallback**: Retorno del último valor cacheado (cache-driven fallback)

**Implementación**: Decorador `@CircuitBreaker` en `PercentageApiAdapter.getPercentage()` con método fallback que lee desde caché.

### 3. **Caché con Caffeine (30 minutos TTL)**

**¿Por qué Caffeine?**
- Alto rendimiento y eficiencia de memoria
- Soporte nativo para TTL (Time To Live)
- Integración transparente con Spring Cache

**Configuración**:
- **Tiempo de expiración**: 30 minutos (expireAfterWrite)
- **Almacenamiento**: Heap memory en la JVM
- **Keyed by**: Clave fija `'lastPercentage'`
- **Fallback strategy**: Si el servicio externo falla, el Circuit Breaker lee desde caché

**Ubicación**: `PercentageApiAdapter.getPercentage()` - wrapper reactivo con WebClient

### 4. **Registro Asíncrono con Interceptor**

**Implementación**:
- **RequestCachingFilter**: Wrapper para capturar body de request (lectura múltiple de stream)
- **CallHistoryInterceptor** (HandlerInterceptor): Captura endpoint, parámetros, respuesta y status en `afterCompletion`
- **SaveCallUseCase**: Delega a repositorio JPA para persistencia
- **CallHistoryRepositoryJpa**: Método `save()` anotado con `@Async("historyExecutor")` para escritura no-bloqueante

**Ventajas**:
- Cero impacto en el tiempo de respuesta (ejecución asíncrona)
- Desacoplamiento del registro de la lógica de negocio
- Pool de threads dedicado (`historyExecutor`)

**Configuración del Thread Pool** (en `AsyncConfig.java`):
```java
- Core Pool Size: 2
- Max Pool Size: 4
- Queue Capacity: 500
- Thread Name Prefix: "history-log-"
```

### 5. **Manejo Centralizado de Excepciones**

**Implementación**: `@RestControllerAdvice` + `@ExceptionHandler` en `GlobalExceptionHandler.java`

**Excepciones manejadas**:
- `BusinessException` → 400 Bad Request
- `ValidationException` → 422 Unprocessable Entity
- `ExternalServiceException` → 502 Bad Gateway
- `MethodArgumentNotValidException` → 422 Unprocessable Entity (agrega todos los errores de validación)
- Exception genérica → 500 Internal Server Error

**Respuesta estándar** (record `ErrorResponse`):
```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "Descripción del error",
  "timestamp": "2025-11-13T10:30:00Z"
}
```

**Features**:
- Limpieza de mensajes (elimina stacktraces antes del primer salto de línea)
- Fallback a mensaje genérico si el error es null
- Agregación de errores de validación con formato "field: message"

## 📦 Estructura del Proyecto

```
src/main/java/com/tekton/tenpo/
├── application/                  # Capa de aplicación (Use Cases & Ports)
│   ├── port/
│   │   ├── in/                  # Puertos de entrada (casos de uso)
│   │   │   ├── CalculatePercentagePort
│   │   │   ├── GetAllHistoryCallsPort
│   │   │   └── SaveCallPort
│   │   └── out/                 # Puertos de salida (adaptadores)
│   │       ├── ExternalPercentagePort
│   │       └── CallHistoryRepositoryPort
│   └── usecase/                 # Implementaciones de casos de uso
│       ├── CalculatePercentageUseCase
│       ├── GetAllHistoryCallsUseCase
│       └── SaveCallUseCase
│
├── domain/                       # Capa de dominio (Modelos)
│   └── model/
│       ├── CalculationPercentageResult (record)
│       ├── CallHistory (record)
│       └── CreateCallHistoryInterceptor (record)
│
└── infrastructure/               # Capa de infraestructura (Adaptadores)
    └── adapters/
        ├── in/                  # Adaptadores de entrada
        │   ├── controller/
        │   │   ├── CalculationController      # POST /api/v1/calculos
        │   │   ├── HistoryCallsController     # GET /api/v1/history
        │   │   ├── dto/
        │   │   │   ├── CalculationRequest
        │   │   │   ├── CalculationResponse
        │   │   │   ├── CallHistoryDto
        │   │   │   └── CreateCallHistoryRequest
        │   │   └── exception/
        │   │       ├── GlobalExceptionHandler
        │   │       ├── ErrorResponse (record)
        │   │       ├── BusinessException
        │   │       ├── ValidationException
        │   │       └── ExternalServiceException
        │   ├── interceptor/
        │   │   └── CallHistoryInterceptor     # HandlerInterceptor para capturar llamadas
        │   ├── filter/
        │   │   └── RequestCachingFilter       # Wrapper para lectura de request body
        │   └── config/
        │       ├── OpenAPIConfig              # Documentación Swagger
        │       ├── WebConfig                  # Registro de interceptor
        │       ├── PercentageApiProperties
        │       ├── async/
        │       │   └── AsyncConfig            # ThreadPoolTaskExecutor para @Async
        │       └── webclient/
        │           └── WebClientConfig        # Bean WebClient reactivo
        │
        └── out/                 # Adaptadores de salida
            ├── external/
            │   ├── PercentageApiAdapter       # WebClient + Cache + CircuitBreaker
            │   └── dto/
            │       └── PercentageApiResponse
            └── persistence/
                ├── CallHistoryEntity          # @Entity JPA
                ├── CallHistoryRepositoryJpa   # Implementación de repositorio
                └── mapper/
                    ├── CallHistoryMapper      # MapStruct mapper
                    └── CalculationResponseMapper  # MapStruct mapper

src/main/resources/
├── application.properties         # Configuración principal
├── application-test.yml          # Perfil de test (Testcontainers)

src/test/java/
└── com/tekton/tenpo/
    ├── application/usecase/      # Tests de casos de uso
    ├── domain/model/             # Tests de modelos
    ├── infrastructure/
    │   ├── adapters/in/
    │   │   ├── controller/       # Tests de controladores
    │   │   ├── config/           # Tests de configuración
    │   │   └── interceptor/      # Tests de interceptor
    │   └── adapters/out/
    │       └── persistence/      # Tests de entidades y mappers
    ├── TenpoApplicationTests.java    # Test de contexto Spring
    └── TestcontainersConfiguration  # Configuración para Testcontainers

docker/
├── Dockerfile                     # Multi-stage build (Maven + JDK 21)
└── docker-compose.yml             # Orquestación (API + PostgreSQL 16 + pgAdmin)
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
git clone https://github.com/danielpinto2407/tekton-tenpo.git
cd tenpo
```

2. **Levantar los servicios**
```bash
docker-compose up -d
```

Esto inicia:
- **tenpo-api**: API en http://localhost:8080
- **postgres**: Base de datos PostgreSQL 16 en localhost:5432
- **pgadmin**: Interfaz gráfica de Postgres en http://localhost:5050

3. **Verificar que los contenedores estén corriendo**
```bash
docker-compose ps
```

4. **Acceder a la aplicación**
- 🌐 API: http://localhost:8080
- 📚 Swagger UI: http://localhost:8080/swagger-ui.html
- 🗄️ pgAdmin: http://localhost:5050 (admin@tenpo.com / admin123)
- 📊 Health Check: http://localhost:8080/actuator/health
- 📈 Métricas: http://localhost:8080/actuator/metrics

5. **Ver logs en tiempo real**
```bash
docker-compose logs -f tenpo-api
```

6. **Detener los servicios**
```bash
docker-compose down
```

7. **Detener y eliminar volúmenes (borrar base de datos)**
```bash
docker-compose down -v
```

### Opción 2: Ejecución Local (sin Docker)

#### Requisitos previos:
- Java 21 instalado
- Maven 3.8+ instalado
- PostgreSQL 15+ ejecutándose localmente

#### Pasos:

1. **Iniciar PostgreSQL localmente** (si no lo tienes instalado)
```bash
# Usar Docker solo para Postgres
docker run -d \
  --name tenpo-postgres \
  -e POSTGRES_DB=tenpo \
  -e POSTGRES_USER=tenpo_user \
  -e POSTGRES_PASSWORD=tenpo_pass \
  -p 5432:5432 \
  postgres:16-alpine
```

2. **Compilar el proyecto**
```bash
./mvnw clean package
```

3. **Ejecutar la aplicación**
```bash
./mvnw spring-boot:run
```

O directamente con el JAR:
```bash
java -jar target/tenpo-0.0.1-SNAPSHOT.jar
```

4. **Acceder a la API**
```
http://localhost:8080/swagger-ui.html
```

### Opción 3: Ejecutar solo Tests

```bash
# Tests unitarios
./mvnw test

# Tests con cobertura JaCoCo
./mvnw clean test jacoco:report

# Ver reporte de cobertura (después de ejecutar comando anterior)
# Windows: start target/site/jacoco/index.html
# macOS: open target/site/jacoco/index.html
# Linux: xdg-open target/site/jacoco/index.html
```

## 📝 Endpoints Principales

### 1. Cálculo con Porcentaje Dinámico
```http
POST /api/v1/calculos
Content-Type: application/json

Request:
{
  "num1": 100,
  "num2": 50
}

Response (200 OK):
{
  "num1": 100.0,
  "num2": 50.0,
  "percentage": 15.5,
  "result": 177.75
}
```

**Lógica**:
1. Suma: 100 + 50 = 150
2. Obtiene porcentaje de https://csrng.net/csrng/csrng.php?min=0&max=100
3. Aplica porcentaje: 150 + (150 * 15.5%) = 177.75
4. Registra automáticamente en historial (asíncrono, sin bloquear respuesta)

**Código de respuesta**:
- `200`: Cálculo exitoso
- `422`: Validación fallida (números debe ser positivos)
- `502`: Servicio externo no disponible (usa caché de fallback)
- `500`: Error inesperado

### 2. Obtener Historial de Llamadas
```http
GET /api/v1/history?page=0&size=10

Response (200 OK):
{
  "content": [
    {
      "id": 1,
      "timestamp": "2025-11-13T10:30:45Z",
      "endpoint": "/api/v1/calculos",
      "parameters": "{\"num1\":100,\"num2\":50}",
      "response": "Calculo realizado",
      "statusCode": 200
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "totalElements": 45,
  "totalPages": 5,
  "last": false,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 10,
  "empty": false
}
```

**Parámetros**:
- `page` (default: 0): Número de página (inicia en 0)
- `size` (default: 10): Cantidad de registros por página

### 3. Health Check
```http
GET /actuator/health

Response (200 OK):
{
  "status": "UP",
  "components": {
    "db": {"status": "UP", "details": {...}},
    "diskSpace": {"status": "UP", "details": {...}},
    "livenessState": {"status": "UP"},
    "readinessState": {"status": "UP"}
  }
}
```

### 4. Métricas
```http
GET /actuator/metrics

GET /actuator/metrics/jvm.memory.usage
GET /actuator/metrics/process.cpu.usage
GET /actuator/metrics/http.server.requests
```

### 5. Circuit Breaker Status
```http
GET /actuator/circuitbreakers

Response:
{
  "circuitBreakers": [
    {
      "name": "percentageApi",
      "state": "CLOSED",
      "failureRate": 0.0,
      "slowCallRate": 0.0,
      "bufferSize": 5
    }
  ]
}
```

## 🧪 Información de Tests

### Test Suite
- **Total**: 59 unit tests
- **Cobertura**: ~70-80% (código productivo, excluyendo DTOs y entidades)
- **Status**: ✅ Todos pasando

### Tests por Módulo
- **Controllers**: `CalculationControllerTest`, `HistoryCallsControllerTest`
- **Use Cases**: `CalculatePercentageUseCaseTest`, `GetAllHistoryCallsUseCaseTest`, `SaveCallUseCaseTest`
- **Adapters**: `PercentageApiAdapterTest`
- **Exception Handler**: `GlobalExceptionHandlerTest` (11 tests, 100% cobertura)
- **Mappers**: `CallHistoryMapperTest`, `CalculationResponseMapperTest`
- **Config**: `OpenAPIConfigTest`, `WebClientConfigTest`, `AsyncConfigTest`, `WebConfigTest`
- **Interceptor**: `CallHistoryInterceptorTest`
- **Entity**: `CallHistoryEntityTest`
- **Models**: `CalculationPercentageResultTest`, `CallHistoryTest`
- **Integration**: `TenpoApplicationTests` (context loading con Testcontainers)

### Ejecutar Tests
```bash
# Todos los tests
./mvnw test

# Tests específicos
./mvnw test -Dtest=CalculationControllerTest

# Con cobertura (genera reporte en target/site/jacoco/)
./mvnw clean test jacoco:report
```

### Testcontainers
El proyecto incluye configuración de Testcontainers para tests de integración:
- `TestcontainersConfiguration.java`: Bean que inicia PostgreSQL automáticamente para tests
- `TestTenpoApplication.java`: Application class para tests con Testcontainers
- Se activa con perfil `test` en `application-test.yml`

## 🔍 Monitoreo y Métricas

### Actuator Endpoints Disponibles

```bash
# Status de salud de la aplicación y componentes
GET http://localhost:8080/actuator/health

# Información sobre la aplicación
GET http://localhost:8080/actuator/info

# Listado de métricas disponibles
GET http://localhost:8080/actuator/metrics

# Métricas específicas (ejemplos)
GET http://localhost:8080/actuator/metrics/jvm.memory.usage
GET http://localhost:8080/actuator/metrics/jvm.memory.committed
GET http://localhost:8080/actuator/metrics/process.cpu.usage
GET http://localhost:8080/actuator/metrics/http.server.requests

# Estado del Circuit Breaker
GET http://localhost:8080/actuator/circuitbreakers

# Detalles del Circuit Breaker (percentageApi)
GET http://localhost:8080/actuator/circuitbreakers/percentageApi

# Cache stats (si estuviera habilitado en actuator)
GET http://localhost:8080/actuator/caches
```

### Logs

```bash
# Logs en tiempo real desde Docker Compose
docker-compose logs -f tenpo-api

# Filtrar por nivel
docker-compose logs tenpo-api | grep ERROR
docker-compose logs tenpo-api | grep WARN

# Últimas líneas
docker-compose logs --tail 50 tenpo-api
```

### Información de la Base de Datos

Acceso vía pgAdmin:
- URL: http://localhost:5050
- Email: admin@tenpo.com
- Password: admin123

**Conexión a Postgres:**
```bash
# Desde la máquina host
psql -h localhost -U tenpo_user -d tenpo

# Desde dentro del contenedor
docker-compose exec postgres psql -U tenpo_user -d tenpo
```

**Consultas útiles en pgAdmin:**
```sql
-- Ver todas las llamadas registradas
SELECT * FROM call_history ORDER BY timestamp DESC;

-- Contar llamadas por endpoint
SELECT endpoint, COUNT(*) as count FROM call_history GROUP BY endpoint;

-- Promedio de status code
SELECT status_code, COUNT(*) FROM call_history GROUP BY status_code;

-- Últimas 10 llamadas
SELECT * FROM call_history ORDER BY timestamp DESC LIMIT 10;
```

## ⚙️ Configuración y Propiedades

### Archivo: `application.properties`

```properties
# Aplicación
spring.application.name=tenpo

# OpenAPI / Swagger
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true

# API Externa (CSRNG.net)
external.percentage.base-url=https://csrng.net
external.percentage.path=/csrng/csrng.php?min=0&max=100

# Resilience4j - Circuit Breaker
resilience4j.circuitbreaker.instances.percentageApi.sliding-window-size=5
resilience4j.circuitbreaker.instances.percentageApi.minimum-number-of-calls=3
resilience4j.circuitbreaker.instances.percentageApi.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.percentageApi.wait-duration-in-open-state=30s
resilience4j.circuitbreaker.instances.percentageApi.permitted-number-of-calls-in-half-open-state=2
resilience4j.circuitbreaker.instances.percentageApi.automatic-transition-from-open-to-half-open-enabled=true

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/tenpo
spring.datasource.username=tenpo_user
spring.datasource.password=tenpo_pass
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### En Docker Compose

Las credenciales se definen en `docker-compose.yml`:
```yaml
postgres:
  environment:
    POSTGRES_DB: tenpo
    POSTGRES_USER: tenpo_user
    POSTGRES_PASSWORD: tenpo_pass
```

La aplicación (servicio `tenpo-api`) recibe las credenciales via variables de entorno:
```yaml
tenpo-api:
  environment:
    SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/tenpo
    SPRING_DATASOURCE_USERNAME: tenpo_user
    SPRING_DATASOURCE_PASSWORD: tenpo_pass
```

## 🐛 Troubleshooting

### La aplicación no inicia

**Error**: `Connection refused` o `Cannot connect to database`

```bash
# Verificar que PostgreSQL esté corriendo
docker-compose ps

# Ver logs de la aplicación
docker-compose logs tenpo-api

# Verificar que Postgres esté listo
docker-compose exec postgres pg_isready

# Reiniciar los servicios
docker-compose down
docker-compose up -d
```

### El servicio externo de porcentajes falla constantemente

**Comportamiento esperado**:
1. Intenta llamar a https://csrng.net
2. Si falla 50% de las llamadas en la ventana de 5 llamadas → **Circuit Breaker se abre**
3. Espera 30 segundos en estado OPEN
4. Automáticamente pasa a HALF_OPEN y prueba la conexión
5. Si tiene caché disponible, la usa (fallback strategy)

**Verificar estado**:
```bash
curl http://localhost:8080/actuator/circuitbreakers/percentageApi
```

**Forzar cierre de circuito** (para testing):
```bash
# Hacer múltiples requests fallidos al cálculo
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/v1/calculos \
    -H "Content-Type: application/json" \
    -d '{"num1": 100, "num2": 50}'
done
```

### Base de datos no conecta

```bash
# Verificar que el container de Postgres esté corriendo
docker ps | grep postgres

# Revisar logs de Postgres
docker-compose logs postgres

# Probar conexión manual
docker-compose exec postgres psql -U tenpo_user -d tenpo

# Verificar variables de entorno en la aplicación
docker-compose exec tenpo-api env | grep DATASOURCE
```

### Puertos ya están en uso

```bash
# Liberar puerto 8080 (API)
# En Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# En Linux/Mac
lsof -i :8080
kill -9 <PID>

# Liberar puerto 5432 (Postgres)
netstat -ano | findstr :5432
# O modificar docker-compose.yml:
# ports:
#   - "5433:5432"  <- cambiar a otro puerto
```

### Problemas de permisos en Docker

```bash
# Reconstruir la imagen sin caché
docker-compose build --no-cache

# Limpiar volúmenes y contenedores
docker-compose down -v

# Reiniciar desde cero
docker-compose up -d
```

### Tests fallan localmente

```bash
# Limpiar y compilar
./mvnw clean compile

# Ejecutar con verbose
./mvnw test -X

# Ejecutar un test específico
./mvnw test -Dtest=CalculationControllerTest#calculate_returnsOkWithResult
```

### Memory Leak o Consumo Alto de Memoria

```bash
# Ver estadísticas de JVM
curl http://localhost:8080/actuator/metrics/jvm.memory.usage

# Aumentar heap en Dockerfile (si es necesario)
# ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
```

## 📚 Documentación Adicional

### OpenAPI / Swagger
- **URL**: http://localhost:8080/swagger-ui.html (cuando la aplicación esté corriendo)
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

Todos los endpoints están documentados con:
- Descripción de operación
- Parámetros requeridos y opcionales
- Esquemas de request/response
- Códigos HTTP esperados

### Dockerfile

**Multi-stage build** (optimizando tamaño de imagen):
1. **Stage 1 (builder)**: Maven 3.9.6 + JDK 21 → Compila y empaqueta el JAR
2. **Stage 2 (runtime)**: JDK 21 → Solo copia el JAR generado

```dockerfile
# Etapa 1: build (descarga dependencias y compila)
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Etapa 2: runtime (ejecuta el JAR)
FROM eclipse-temurin:21-jdk
```

**Ventajas**:
- Imagen final más pequeña (~600 MB vs 2GB)
- Dependencias de build no incluidas en producción
- Mejor seguridad

### Docker Compose

**Servicios**:
1. **postgres**: PostgreSQL 16 Alpine
   - Volumen: `pgdata` (persistencia)
   - Red: `tenpo-net`

2. **tenpo-api**: Aplicación Spring Boot
   - Build: `./Dockerfile`
   - Depende de: `postgres`
   - Expone: Puerto 8080

3. **pgadmin**: Interfaz gráfica para Postgres
   - Expone: Puerto 5050
   - Credenciales: admin@tenpo.com / admin123

**Red personalizada**: `tenpo-net` (bridge driver)
- Permite que los contenedores se comuniquen por nombre de servicio

### Tecnologías Específicas

#### Resilience4j Circuit Breaker
- **Patrón**: Protection por defecto contra servicios externos inestables
- **Estados**: CLOSED (normal) → OPEN (bloqueado) → HALF_OPEN (testing)
- **Configuración**: `application.properties`
- **Métricas**: `/actuator/circuitbreakers/percentageApi`

#### Caffeine Cache
- **Implementación**: InMemory con expireAfterWrite de 30 minutos
- **Key**: `lastPercentage`
- **Fallback**: Si el servicio externo no responde, lee desde caché

#### MapStruct
- **Mappers**: `CallHistoryMapper`, `CalculationResponseMapper`
- **Compilación**: Annotation processing en fase de build
- **Ventajas**: Zero runtime overhead vs ObjectMapper/ModelMapper

#### Lombok
- **Anotaciones**: `@Data`, `@RequiredArgsConstructor`, `@Slf4j`
- **Reduce**: Boilerplate (getters, setters, constructores)
- **Records**: Modelos de dominio como Java Records (inmutables)

### Patrones Utilizados

1. **Hexagonal Architecture**: Puertos & Adaptadores
2. **Circuit Breaker**: Resilience4j
3. **Caching**: Cache-aside pattern (manual)
4. **CQRS-lite**: Controllers → UseCases → Repositories
5. **Async Processing**: @Async para historial
6. **Exception Translation**: Global ExceptionHandler

## 👥 Contribución

Pasos para contribuir:

1. **Fork el proyecto**
   ```bash
   # En GitHub, click en "Fork"
   ```

2. **Crear rama de feature**
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```

3. **Hacer cambios y commit**
   ```bash
   git add .
   git commit -m "Agregar nueva funcionalidad"
   ```

4. **Empujar a tu fork**
   ```bash
   git push origin feature/nueva-funcionalidad
   ```

5. **Crear Pull Request**
   - En GitHub, clic en "Compare & pull request"
   - Describe el cambio y beneficios
   - Espera revisión

## 📋 Pre-requisitos de Contribución

Antes de hacer PR, asegurate que:

```bash
# 1. Tests pasen
./mvnw test

# 2. Cobertura sea adecuada
./mvnw clean test jacoco:report
# Revisar target/site/jacoco/index.html

# 3. Código esté formateado
# (Tu IDE debería aplicar formateo automático)

# 4. No haya errores de compilación
./mvnw clean compile
```

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## ✅ Checklist de Requisitos Completados

### Funcionalidad Core
- [x] Cálculo con porcentaje dinámico desde API externa (CSRNG.net)
- [x] Obtención del porcentaje con WebClient reactivo
- [x] Aplicación correcta de la fórmula: (num1 + num2) + ((num1 + num2) * percentage / 100)
- [x] Validación de inputs positivos (@Positive)

### Resiliencia y Caché
- [x] Circuit Breaker con Resilience4j (fail-fast protection)
- [x] Caché Caffeine con TTL de 30 minutos (expireAfterWrite)
- [x] Fallback strategy: usar caché cuando servicio externo falla
- [x] Monitoreo via `/actuator/circuitbreakers/percentageApi`

### Persistencia e Historial
- [x] Historial de llamadas en PostgreSQL (Spring Data JPA)
- [x] Entity: `CallHistoryEntity` con Lombok
- [x] Repositorio: `CallHistoryRepositoryJpa`
- [x] Paginación (PageRequest con page/size)
- [x] Registro asíncrono via `@Async` (sin bloquear respuesta)

### Captura de Requestshistoria
- [x] Interceptor: `CallHistoryInterceptor` (HandlerInterceptor)
- [x] Filter: `RequestCachingFilter` (lectura de request body)
- [x] Información capturada: endpoint, parámetros, respuesta, statusCode, timestamp
- [x] Ejecución asíncrona con ThreadPoolTaskExecutor

### API REST y Documentación
- [x] Controller: POST `/api/v1/calculos` (cálculo)
- [x] Controller: GET `/api/v1/history?page=0&size=10` (historial)
- [x] OpenAPI/Swagger en `/swagger-ui.html`
- [x] Documentación de endpoints con anotaciones Swagger
- [x] DTOs con validación (@Valid, @NotNull, @Positive)

### Manejo de Errores
- [x] Exception centralizado: `@RestControllerAdvice`
- [x] `BusinessException` → 400 Bad Request
- [x] `ValidationException` → 422 Unprocessable Entity
- [x] `ExternalServiceException` → 502 Bad Gateway
- [x] Manejo de validación de campos → 422 con detalles
- [x] Response estándar: record `ErrorResponse`

### Configuración
- [x] `application.properties` con credenciales de BD
- [x] Resilience4j properties: sliding-window, failure-rate, wait-duration
- [x] OpenAPI properties: paths de swagger-ui
- [x] Perfil de test con Testcontainers

### Docker y Deployment
- [x] Dockerfile multi-stage (Maven → JDK 21)
- [x] docker-compose.yml con 3 servicios: API, Postgres, pgAdmin
- [x] Volumen persistente para Postgres
- [x] Red personalizada (tenpo-net)
- [x] Variables de entorno correctamente mapeadas

### Testing
- [x] 59 unit tests pasando (0 errores, 0 fallos)
- [x] Tests unitarios para cada capa:
  - Controllers (MockMvc)
  - Use Cases (Mockito)
  - Adapter externo (cache + fallback)
  - Exception handler (11 tests, 100%)
  - Mappers (MapStruct round-trip)
  - Configuración (beans)
  - Interceptor (captura)
  - Entity (builders)
- [x] Test de contexto Spring: `TenpoApplicationTests` con Testcontainers
- [x] JaCoCo configurado con exclusiones inteligentes (DTOs, builders, etc.)

### Code Quality
- [x] Arquitectura hexagonal bien separada
- [x] Patterns: Circuit Breaker, Cache-aside, @Async, Interceptor
- [x] Manejo de dependencias inyectado con Spring
- [x] Records para modelos de dominio (immutables)
- [x] Logging con @Slf4j

## 📈 Cobertura JaCoCo

Para ver la cobertura exacta:
```bash
./mvnw clean test jacoco:report
# Abre: target/site/jacoco/index.html
```

**Clases Excluidas** (por diseño):
- DTOs (`CalculationRequest`, `CalculationResponse`, etc.)
- Mappers (`CallHistoryMapper`, `CalculationResponseMapper`)
- Entity Lombok generated (`CallHistoryEntity$*`)
- Filtros de utilidad (`RequestCachingFilter`)
- Bootstrap (`TenpoApplication`)

## 📧 Contacto

Para preguntas o sugerencias, contactar a: [wdpinto@utp.edu.co]

---

**Desarrollado con ☕ y Spring Boot**