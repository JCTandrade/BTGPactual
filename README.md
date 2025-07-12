# 🏦 Plataforma de Gestión de Fondos de Inversión BTG Pactual

## 📋 Descripción

Plataforma que permite a los clientes gestionar sus fondos de inversión sin necesidad de contactar a un asesor. Desarrollada con Spring Boot, implementa todas las funcionalidades requeridas siguiendo las mejores prácticas de desarrollo.

## 🚀 Funcionalidades Implementadas

### ✅ Funcionalidades del Sistema
1. **Suscribirse a un nuevo fondo** - Apertura de suscripciones
2. **Cancelar suscripción** - Cancelación de fondos actuales
3. **Ver historial de transacciones** - Consulta de aperturas y cancelaciones
4. **Notificaciones automáticas** - Email y SMS según preferencia del usuario

### ✅ Reglas de Negocio Implementadas
- ✅ Monto inicial del cliente: COP $500.000
- ✅ Identificador único para cada transacción
- ✅ Validación de montos mínimos por fondo
- ✅ Devolución del valor al cancelar suscripción
- ✅ Validación de saldo insuficiente con mensaje personalizado

## 🏗️ Arquitectura del Proyecto

### Estructura de Paquetes
```
src/main/java/com/gft/BTGPactual/
├── config/           # Configuraciones (Security, DataInitializer)
├── controller/       # Controladores REST
├── dto/             # Objetos de transferencia de datos
├── exception/       # Excepciones personalizadas
├── model/           # Entidades JPA
├── repository/      # Repositorios de datos
└── service/         # Lógica de negocio
```

### Tecnologías Utilizadas
- **Spring Boot 3.5.3** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **H2 Database** - Base de datos en memoria (desarrollo)
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias

## 📊 Modelo de Datos

### Fondos de Inversión Disponibles
| ID | Nombre | Monto Mínimo | Categoría |
|----|--------|--------------|-----------|
| 1 | FPV_BTG_PACTUAL_RECAUDADORA COP | COP $75.000 | FPV |
| 2 | FPV_BTG_PACTUAL_ECOPETROL | COP $125.000 | FPV |
| 3 | DEUDAPRIVADA | COP $50.000 | FIC |
| 4 | FDO-ACCIONES | COP $250.000 | FIC |
| 5 | FPV_BTG_PACTUAL_DINAMICA | COP $100.000 | FPV |

## 🔌 API REST

### Endpoints Principales

#### 1. Suscribirse a un Fondo
```http
POST /api/v1/fondos/suscribirse
Content-Type: application/json

{
  "clienteId": 1,
  "fondoId": 1,
  "montoVinculado": 100000.00
}
```

#### 2. Cancelar Suscripción
```http
POST /api/v1/fondos/cancelar
Content-Type: application/json

{
  "clienteId": 1,
  "fondoId": 1
}
```

#### 3. Historial de Transacciones
```http
GET /api/v1/fondos/transacciones/{clienteId}
```

#### 4. Listar Fondos Disponibles
```http
GET /api/v1/admin/fondos
```

#### 5. Listar Clientes
```http
GET /api/v1/admin/clientes
```

### Respuestas de Ejemplo

#### Suscripción Exitosa
```json
{
  "identificadorTransaccion": "TXN-A1B2C3D4",
  "nombreCliente": "Juan Carlos Tamayo",
  "nombreFondo": "FPV_BTG_PACTUAL_RECAUDADORA COP",
  "tipoTransaccion": "SUSCRIPCION",
  "monto": 100000.00,
  "fechaTransaccion": "2025-07-12T00:30:00",
  "descripcion": "Suscripción al fondo FPV_BTG_PACTUAL_RECAUDADORA COP",
  "estado": "EXITOSA"
}
```

#### Error de Saldo Insuficiente
```json
{
  "status": 400,
  "message": "No tiene saldo disponible para vincularse al fondo FDO-ACCIONES",
  "timestamp": "2025-07-12T00:30:00"
}
```

## 🛡️ Seguridad

### Configuración Implementada
- **Spring Security** con configuración personalizada
- **BCrypt** para encriptación de contraseñas
- **Autorización por roles** (ADMIN para endpoints administrativos)
- **CSRF deshabilitado** para APIs REST
- **Headers de seguridad** configurados

### Roles y Permisos
- **Público**: Endpoints de gestión de fondos (`/api/v1/fondos/**`)
- **ADMIN**: Endpoints administrativos (`/api/v1/admin/**`)
- **Autenticado**: Otros endpoints

## 🧪 Pruebas

### Pruebas Unitarias
- ✅ Servicios de negocio
- ✅ Validaciones de reglas de negocio
- ✅ Manejo de excepciones
- ✅ Casos de éxito y error

### Ejecutar Pruebas
```bash
mvn test
```

## 🚀 Despliegue

### Requisitos
- Java 17+
- Maven 3.6+

### Ejecutar Localmente
```bash
# Clonar el repositorio
git clone <repository-url>
cd BTGPactual

# Compilar y ejecutar
mvn spring-boot:run
```

### Acceso a la Aplicación
- **API REST**: http://localhost:8080/api/v1/
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: `password`

### Datos de Prueba
La aplicación se inicializa automáticamente con:
- 5 fondos de inversión
- 3 clientes de prueba con saldo inicial de COP $500.000

## 📝 Manejo de Excepciones

### Excepciones Personalizadas
- `SaldoInsuficienteException` - Saldo insuficiente para suscripción
- `RecursoNoEncontradoException` - Cliente o fondo no encontrado
- `SuscripcionExistenteException` - Ya existe suscripción activa

### Respuestas de Error Estandarizadas
```json
{
  "status": 400,
  "message": "Mensaje descriptivo del error",
  "timestamp": "2025-07-12T00:30:00"
}
```

## 🔧 Configuración

### Archivo `application.properties`
```properties
# Base de datos H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Seguridad
spring.security.user.name=admin
spring.security.user.password=admin
```

## 📈 Próximos Pasos

### Mejoras Futuras
1. **Integración con AWS**:
   - AWS RDS para base de datos
   - AWS SES para emails
   - AWS SNS para SMS
   - AWS CloudFormation para infraestructura

2. **Funcionalidades Adicionales**:
   - Dashboard de administración
   - Reportes y analytics
   - Integración con sistemas externos
   - API de consulta de saldos

3. **Seguridad Avanzada**:
   - JWT para autenticación
   - Rate limiting
   - Auditoría de transacciones
   - Encriptación de datos sensibles

## 👥 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 📞 Contacto

- **Desarrollador**: Juan Carlos Tamayo
- **Email**: juan.tamayo@email.com
- **Proyecto**: BTG Pactual - Gestión de Fondos de Inversión

---

**¡Gracias por usar la Plataforma de Gestión de Fondos BTG Pactual!** 🚀 