# 🏦 Plataforma de Gestión de Fondos de Inversión BTG Pactual

## 📋 Descripción

Plataforma avanzada de gestión de fondos de inversión desarrollada con Spring Boot 3.5.3, que permite a los clientes gestionar sus inversiones sin necesidad de contactar a un asesor. El sistema implementa autenticación JWT, integración con AWS DynamoDB, notificaciones automáticas y todas las funcionalidades requeridas siguiendo las mejores prácticas de desarrollo.

## 🚀 Funcionalidades Implementadas

### ✅ Funcionalidades del Sistema
1. **Autenticación JWT** - Sistema de login seguro con tokens
2. **Suscribirse a un nuevo fondo** - Apertura de suscripciones
3. **Cancelar suscripción** - Cancelación de fondos actuales
4. **Ver historial de transacciones** - Consulta de aperturas y cancelaciones
5. **Notificaciones automáticas** - Email y SMS según preferencia del usuario
6. **Gestión de usuarios** - Roles ADMIN y USER
7. **Integración AWS** - DynamoDB, SES, SNS

### ✅ Reglas de Negocio Implementadas
- ✅ Monto inicial del cliente: COP $500.000
- ✅ Identificador único para cada transacción
- ✅ Validación de montos mínimos por fondo
- ✅ Devolución del valor al cancelar suscripción
- ✅ Validación de saldo insuficiente con mensaje personalizado
- ✅ Prevención de suscripciones duplicadas
- ✅ Autenticación y autorización por roles

## 🏗️ Arquitectura del Proyecto

### Estructura de Paquetes
```
src/main/java/com/gft/BTGPactual/
├── config/           # Configuraciones (Security, AWS, JWT, DataInitializer)
├── controller/       # Controladores REST (Auth, Admin, GestionFondos)
├── dto/             # Objetos de transferencia de datos
├── exception/       # Excepciones personalizadas
├── model/           # Entidades (Cliente, Fondo, Suscripcion, Transaccion, Usuario)
├── service/         # Lógica de negocio e interfaces
│   └── impl/        # Implementaciones de servicios
└── BtgPactualApplication.java
```

### Tecnologías Utilizadas
- **Spring Boot 3.5.3** - Framework principal
- **Spring Security** - Autenticación y autorización con JWT
- **Spring Data JPA** - Persistencia de datos
- **AWS SDK v2** - Integración con servicios AWS
    - DynamoDB - Base de datos NoSQL
    - SES - Servicio de email
    - SNS - Servicio de notificaciones SMS
- **JWT (JSON Web Tokens)** - Autenticación stateless
- **H2 Database** - Base de datos en memoria (desarrollo)
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias

## 🔐 Sistema de Autenticación

### Autenticación JWT
El sistema utiliza JSON Web Tokens para autenticación stateless:

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

#### Respuesta de Login
```json
{
  "username": "admin",
  "roles": ["ADMIN"],
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Uso del Token
```http
GET /api/v1/admin/fondos
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Roles y Permisos
- **ADMIN**: Acceso completo a endpoints administrativos
- **USER**: Acceso a funcionalidades básicas de gestión de fondos

### Configuración de Seguridad
- **JWT Secret**: Configurado via variable de entorno `JWT_SECRET`
- **Expiración de tokens**: Configurable
- **BCrypt**: Encriptación de contraseñas
- **CSRF**: Deshabilitado para APIs REST

## 📊 Modelo de Datos

### Entidades Principales

#### Usuario
```java
public class Usuario {
    private String id;
    private String username;
    private String password;
    private List<Rol> roles; // ADMIN, USER
}
```

#### Cliente
```java
public class Cliente {
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private BigDecimal saldo; // Inicial: COP $500.000
    private TipoNotificacion tipoNotificacion; // EMAIL, SMS, AMBOS
    private boolean activo;
}
```

#### Fondo
```java
public class Fondo {
    private String id;
    private String nombre;
    private BigDecimal montoMinimo;
    private String categoria; // FPV, FIC
}
```

### Fondos de Inversión Disponibles
| ID | Nombre | Monto Mínimo | Categoría |
|----|--------|--------------|-----------|
| 1 | FPV_BTG_PACTUAL_RECAUDADORA COP | COP $75.000 | FPV |
| 2 | FPV_BTG_PACTUAL_ECOPETROL | COP $125.000 | FPV |
| 3 | DEUDAPRIVADA | COP $50.000 | FIC |
| 4 | FDO-ACCIONES | COP $250.000 | FIC |
| 5 | FPV_BTG_PACTUAL_DINAMICA | COP $100.000 | FPV |

## 🔌 API REST

### Endpoints de Autenticación

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### Endpoints de Gestión de Fondos

#### 1. Suscribirse a un Fondo
```http
POST /api/v1/fondos/suscribirse
Content-Type: application/json

{
  "clienteId": "1",
  "fondoId": "1",
  "montoVinculado": 100000.00
}
```

#### 2. Cancelar Suscripción
```http
POST /api/v1/fondos/cancelar
Content-Type: application/json

{
  "clienteId": "1",
  "fondoId": "1"
}
```

#### 3. Historial de Transacciones
```http
GET /api/v1/fondos/transacciones/{clienteId}
```

### Endpoints Administrativos (Requieren rol ADMIN)

#### 4. Listar Fondos Disponibles
```http
GET /api/v1/admin/fondos
Authorization: Bearer <token>
```

#### 5. Listar Clientes
```http
GET /api/v1/admin/clientes
Authorization: Bearer <token>
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

## ☁️ Integración AWS

### Servicios AWS Utilizados

#### DynamoDB
- **Tablas**: fondos, clientes, suscripciones, transacciones, usuarios
- **Configuración**: Pay-per-request billing
- **Inicialización automática**: Las tablas se crean automáticamente al iniciar la aplicación

#### SES (Simple Email Service)
- **Propósito**: Envío de notificaciones por email
- **Configuración**: Variable de entorno `SES_IDENTITY_EMAIL`

#### SNS (Simple Notification Service)
- **Propósito**: Envío de notificaciones SMS
- **Configuración**: Integrado con el sistema de notificaciones

### Configuración AWS
```properties
aws.region=us-east-1
aws.access.key.id=${AWS_ACCESS_KEY_ID}
aws.secret.access.key=${AWS_SECRET_ACCESS_KEY}
aws.dynamodb.fondos-table=fondos
aws.dynamodb.clientes-table=clientes
aws.dynamodb.suscripciones-table=suscripciones
aws.dynamodb.transacciones-table=transacciones
aws.dynamodb.usuarios-table=usuarios
```

## 🛡️ Seguridad

### Configuración Implementada
- **Spring Security** con JWT
- **BCrypt** para encriptación de contraseñas
- **Autorización por roles** (ADMIN para endpoints administrativos)
- **CSRF deshabilitado** para APIs REST
- **Headers de seguridad** configurados
- **Filtros JWT** para validación de tokens

### Variables de Entorno Requeridas
```bash
AWS_ACCESS_KEY_ID=tu_access_key
AWS_SECRET_ACCESS_KEY=tu_secret_key
JWT_SECRET=tu_jwt_secret_base64
SES_IDENTITY_EMAIL=tu_email_verificado@dominio.com
```

## 🧪 Pruebas

### Pruebas Unitarias
- ✅ Servicios de negocio
- ✅ Validaciones de reglas de negocio
- ✅ Manejo de excepciones
- ✅ Casos de éxito y error

### Scripts de Prueba
- `test-auth.ps1` - Pruebas de autenticación
- `test-token-validation.ps1` - Validación de tokens
- `test-usuarios.ps1` - Pruebas de usuarios

### Ejecutar Pruebas
```bash
mvn test
```

## 🚀 Despliegue

### Requisitos
- Java 17+
- Maven 3.6+
- Credenciales AWS configuradas

### Ejecutar Localmente
```bash
# Clonar el repositorio
git clone <repository-url>
cd BTGPactual

# Configurar variables de entorno
export AWS_ACCESS_KEY_ID=tu_access_key
export AWS_SECRET_ACCESS_KEY=tu_secret_key
export JWT_SECRET=tu_jwt_secret_base64
export SES_IDENTITY_EMAIL=tu_email@dominio.com

# Compilar y ejecutar
mvn spring-boot:run
```

### Docker
```bash
# Construir imagen
docker build -t btgpactual .

# Ejecutar contenedor
docker run -p 8080:8080 \
  -e AWS_ACCESS_KEY_ID=tu_access_key \
  -e AWS_SECRET_ACCESS_KEY=tu_secret_key \
  -e JWT_SECRET=tu_jwt_secret \
  -e SES_IDENTITY_EMAIL=tu_email@dominio.com \
  btgpactual
```

### Acceso a la Aplicación
- **API REST**: http://localhost:8080/api/v1/
- **H2 Console**: http://localhost:8080/h2-console (solo desarrollo)
    - JDBC URL: `jdbc:h2:mem:testdb`
    - Username: `sa`
    - Password: `password`

### Datos de Prueba
La aplicación se inicializa automáticamente con:
- **Usuario administrador**: admin/admin123
- 5 fondos de inversión
- 3 clientes de prueba con saldo inicial de COP $500.000

## 📝 Manejo de Excepciones

### Excepciones Personalizadas
- `CredencialesInvalidasException` - Credenciales de login incorrectas
- `SaldoInsuficienteException` - Saldo insuficiente para suscripción
- `RecursoNoEncontradoException` - Cliente o fondo no encontrado
- `SuscripcionExistenteException` - Ya existe suscripción activa
- `UsuarioBloqueadoException` - Usuario bloqueado

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
# Aplicación
spring.application.name=BTGPactual
server.port=8080

# Base de datos H2 (desarrollo)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver

# AWS
aws.region=us-east-1
aws.access.key.id=${AWS_ACCESS_KEY_ID}
aws.secret.access.key=${AWS_SECRET_ACCESS_KEY}

# JWT
jwt.secret=${JWT_SECRET}

# Email
email.sender=${SES_IDENTITY_EMAIL}

# DynamoDB Tables
aws.dynamodb.fondos-table=fondos
aws.dynamodb.clientes-table=clientes
aws.dynamodb.suscripciones-table=suscripciones
aws.dynamodb.transacciones-table=transacciones
aws.dynamodb.usuarios-table=usuarios

# Logging
logging.level.com.gft.BTGPactual=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.software.amazon.awssdk=DEBUG
```

## 📈 Características Avanzadas

### Notificaciones Automáticas
- **Email**: Via AWS SES
- **SMS**: Via AWS SNS
- **Configuración por cliente**: EMAIL, SMS, AMBOS
- **Notificaciones automáticas** en suscripciones y cancelaciones

### Gestión de Transacciones
- **Identificadores únicos** para cada transacción
- **Historial completo** de operaciones
- **Estados de transacción**: EXITOSA, FALLIDA, PENDIENTE
- **Ordenamiento por fecha** (más recientes primero)

### Validaciones de Negocio
- **Monto mínimo** por fondo
- **Saldo disponible** del cliente
- **Suscripciones únicas** por cliente-fondo
- **Devolución automática** al cancelar

## 🔄 Flujo de Trabajo

### 1. Autenticación
1. Cliente hace login con credenciales
2. Sistema valida credenciales contra DynamoDB
3. Se genera token JWT con roles del usuario
4. Token se devuelve al cliente

### 2. Suscripción a Fondo
1. Cliente envía solicitud con token JWT
2. Sistema valida token y permisos
3. Se verifica saldo disponible
4. Se valida monto mínimo del fondo
5. Se crea suscripción en DynamoDB
6. Se registra transacción
7. Se envía notificación al cliente

### 3. Cancelación de Suscripción
1. Cliente envía solicitud de cancelación
2. Sistema valida suscripción activa
3. Se calcula monto a devolver
4. Se actualiza saldo del cliente
5. Se marca suscripción como cancelada
6. Se registra transacción
7. Se envía notificación