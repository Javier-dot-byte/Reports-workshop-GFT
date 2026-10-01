# Swagger — Reporting Service

## ¿Qué es Swagger?

Swagger (OpenAPI) es una herramienta que genera documentación interactiva de tu API REST automáticamente. Con ella puedes ver todos tus endpoints, sus parámetros, sus respuestas y probarlos directamente desde el navegador sin necesidad de Postman.

---

## Configuración en el proyecto

### 1. Dependencia en `pom.xml`

Ya la tienes añadida:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

### 2. Configuración en `application.properties`

```properties
# Swagger UI
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.enabled=true

# Opcional: ordenar endpoints por método HTTP
springdoc.swagger-ui.operationsSorter=method

# Opcional: mostrar los modelos expandidos por defecto
springdoc.swagger-ui.defaultModelsExpandDepth=1
```

### 3. URLs de acceso

Con la app arrancada en local:

| URL | Para qué |
|---|---|
| `http://localhost:8080/swagger-ui/index.html` | Interfaz visual interactiva |
| `http://localhost:8080/api-docs` | JSON con la especificación OpenAPI |
| `http://localhost:8080/api-docs.yaml` | YAML con la especificación OpenAPI |

---

## Anotaciones en el código

### En el Controller

```java
@RestController
@RequestMapping("/reports")
@Tag(name = "Event Log", description = "Endpoints para consultar el log de eventos del sistema")
@RequiredArgsConstructor
public class EventLogController {

    private final EventLogServiceImpl eventLogService;

    @Operation(
        summary = "Obtener todos los eventos",
        description = "Devuelve el historial completo de eventos del sistema ordenados por fecha"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de eventos obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/events")
    public ResponseEntity<List<EventLog>> getEvents() {
        return ResponseEntity.ok(eventLogService.findAll());
    }

    @Operation(
        summary = "Obtener eventos paginados",
        description = "Devuelve los eventos paginados para evitar cargar todo en memoria"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de eventos obtenida correctamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos")
    })
    @GetMapping("/events/paged")
    public ResponseEntity<Page<EventLog>> getEventsPaged(
        @Parameter(description = "Número de página (empieza en 0)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(eventLogService.findAllPaged(PageRequest.of(page, size)));
    }
}
```

### En el BlockedOrderController

```java
@RestController
@RequestMapping("/reports")
@Tag(name = "Blocked Orders", description = "Endpoints para consultar pedidos bloqueados")
@RequiredArgsConstructor
public class BlockedOrderController {

    private final BlockedOrderServiceImpl blockedOrderService;

    @Operation(
        summary = "Obtener pedidos bloqueados",
        description = "Devuelve todos los pedidos que están bloqueados por falta de materiales"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos bloqueados obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/blocked-orders")
    public ResponseEntity<List<BlockedOrder>> getBlockedOrders() {
        return ResponseEntity.ok(blockedOrderService.findAll());
    }
}
```

### En los modelos (DTOs o entidades de dominio)

```java
@Schema(description = "Evento del sistema registrado en el log")
public class EventLog {

    @Schema(description = "Identificador único del evento", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Tipo de evento", example = "PRODUCTION_ORDER_CREATED")
    private EventType eventType;

    @Schema(description = "Servicio que originó el evento", example = "PRODUCTION")
    private SourceService sourceService;

    @Schema(description = "Payload JSON completo del evento recibido por RabbitMQ")
    private String payload;

    @Schema(description = "Día de simulación en que ocurrió el evento", example = "5")
    private Integer simulationDay;

    @Schema(description = "Timestamp de cuando ocurrió el evento", example = "2026-05-07T10:00:00")
    private String occurredAt;
}
```

---

## Configuración global de Swagger (opcional pero recomendado)

Crea una clase `OpenApiConfig.java` en `config/`:

```java
package org.example.reportsworskhopgft.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reportingServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reporting Service API")
                        .description("Microservicio de reportes del sistema de simulación logística. " +
                                "Consume eventos de RabbitMQ y expone endpoints para consultar el historial de eventos y pedidos bloqueados.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Grupo Pablo - Reporting")
                                .email("jrtu@gft.com")));
    }
}
```

---

## Imports necesarios

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
```

---

## Endpoints documentados del Reporting Service

| Método | URL | Descripción | Tag |
|---|---|---|---|
| `GET` | `/reports/events` | Todos los eventos del sistema | Event Log |
| `GET` | `/reports/events/paged` | Eventos paginados | Event Log |
| `GET` | `/reports/blocked-orders` | Pedidos bloqueados actualmente | Blocked Orders |
| `GET` | `/reports/stats` | Estadísticas del sistema | Stats |
| `GET` | `/reports/orders/history` | Historial de pedidos | Orders |

---

## Cómo probar desde Swagger UI

1. Arranca la app
2. Ve a `http://localhost:8080/swagger-ui/index.html`
3. Expande el grupo de endpoints que quieres probar (Event Log, Blocked Orders, etc.)
4. Clic en el endpoint
5. Clic en **Try it out**
6. Rellena los parámetros si los hay
7. Clic en **Execute**
8. Ver la respuesta en el recuadro de abajo

---

## Cómo deshabilitar Swagger en producción

Si no quieres que Swagger esté disponible en el entorno de AWS, añade en `application.properties`:

```properties
# Local
springdoc.swagger-ui.enabled=true

# En producción (crear application-prod.properties)
springdoc.swagger-ui.enabled=false
springdoc.api-docs.enabled=false
```

Y arrancas con el perfil de producción:

```
java -jar app.jar --spring.profiles.active=prod
```
