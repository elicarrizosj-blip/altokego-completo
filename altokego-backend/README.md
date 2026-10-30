# AltokeGo

**Lenguajes de Programación II (segundo entregable) y Desarrollo de Aplicativos (presentación final)**

## Integrantes

* Carrizo Eliana
* Diaz Carolina
* Torres Maria

## Objetivo del sistema

AltokeGo es una aplicación web con API REST para la gestión de un servicio de transporte local. Permite registrar pasajeros y los viajes que solicitan, con registro de usuarios, autenticación y permisos según el rol.

Tecnologías: Java 21, Spring Boot 3.2.5, Maven, Spring Data JPA, MySQL, Spring Security (Basic Auth), validaciones con `@Valid` y BCrypt.

## Entidades y relaciones

| Entidad | Atributos principales |
|---|---|
| `Pasajero` | id, nombre, email (único), telefono |
| `Viaje` | id, origen, destino, precioArs, metodoPago, estado, fechaCreacion, pasajero |
| `Usuario` | id, nombre, email (único), password (hash BCrypt), rol (`USER` o `ADMIN`) |

* **Relación JPA real:** un `Pasajero` tiene muchos `Viaje` (`@ManyToOne` desde `Viaje` hacia `Pasajero`). La consulta que usa esa relación es `GET /api/viajes/pasajero/{pasajeroId}`.
* `Usuario` representa las cuentas de acceso a la API (login y roles) y es independiente del dominio de transporte.
* Un viaje no puede crearse con un pasajero inexistente (404) y un pasajero con viajes no puede eliminarse (409).

Estructura del código: `controller`, `service`, `repository`, `entity`, `dto`, `mapper`, `exception` y `security`.

## Cambios respecto de la primera entrega

* Se aplicaron las observaciones de la primera corrección.
* **Registro de usuarios** (`POST /api/auth/register`) con DTO propio, validaciones y contraseña de al menos 6 caracteres.
* **Seguridad con Spring Security:** `SecurityFilterChain` con Basic Auth, sin sesiones (`STATELESS`) y CSRF desactivado por tratarse de una API REST.
* **Contraseñas con BCrypt:** solo se guarda el hash (`PasswordEncoder`). Ninguna respuesta incluye la contraseña ni el hash.
* **Roles:** `USER` y `ADMIN`. El rol se guarda en la base y se carga como autoridad en `CustomUserDetailsService`.
* **Correo repetido:** se comprueba en el service (`ConflictoException`, 409) y además hay una restricción única en la base. El `GlobalExceptionHandler` también traduce la violación de esa restricción a 409.
* Se agregó `UsuarioMapper` y se simplificó `AuthController`: los errores los resuelve el `GlobalExceptionHandler`.
* Se corrigió la etiqueta `<name>` del `pom.xml`.
* **Para la presentación final:** interfaz web tipo app de viajes (`index.html`) servida por la propia aplicación, datos de demo que se cargan al arrancar (`DataInitializer`), el endpoint `GET /api/auth/me` (usuario y rol conectados) y `PATCH /api/viajes/{id}/estado` (solo ADMIN). Los estados de un viaje pasan a ser `pendiente`, `en curso`, `finalizado` y `cancelado`, igual que en el informe de Bases de Datos.
* Se actualizó la colección de Postman con autenticación y las pruebas de la entrega.

## Cómo ejecutar el proyecto

### Requisitos

* Java 21
* MySQL en ejecución (por ejemplo con XAMPP)

### Pasos

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/CaroDiaz02/altokego-backend.git
   cd altokego-backend
   ```

2. Crear la base de datos vacía en MySQL (las tablas las crea Hibernate con `ddl-auto=update`):

   ```sql
   CREATE DATABASE IF NOT EXISTS altokego_db;
   ```

3. Revisar `src/main/resources/application.properties`. Por defecto usa `root` sin contraseña (configuración local de XAMPP). Si tu MySQL tiene otra clave, cámbiala solo en tu copia local.

4. Ejecutar la aplicación (elegir una forma):

   * **Desde VS Code:** instalar las extensiones *Extension Pack for Java* y *Spring Boot Extension Pack*, abrir la carpeta del proyecto (la que contiene `pom.xml`), esperar a que termine de cargar, abrir `src/main/java/com/altokego/backend/BackendApplication.java` y tocar **Run** sobre el método `main`.
   * **Con Maven instalado:** `mvn spring-boot:run` en la carpeta del `pom.xml`.

   Al arrancar, la consola muestra un mensaje `Started BackendApplication`. La aplicación queda en `http://localhost:8080`. No hay que usar la extensión *Live Server*: sirve archivos estáticos y no ejecuta Java.

## Datos de prueba y usuarios ficticios

Las credenciales de abajo son ficticias y se crean solo para esta entrega.

| Rol | Correo (username) | Contraseña |
|---|---|---|
| USER | `usuario.prueba@altokego.test` | `prueba123` |
| ADMIN | `admin.prueba@altokego.test` | `admin123` |

**Registrar el usuario de prueba (rol USER):**

```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "nombre": "Usuario Prueba",
  "email": "usuario.prueba@altokego.test",
  "password": "prueba123"
}
```

**Preparar un administrador de prueba.** El registro público siempre asigna `USER` y no permite elegir `ADMIN`. Para crear el administrador se registra primero la cuenta (ver `Registrar administrador de prueba` en la colección de Postman, con el correo `admin.prueba@altokego.test` y la contraseña `admin123`) y después se cambia el rol directamente en la base:

```sql
USE altokego_db;
UPDATE usuarios SET rol = 'ADMIN' WHERE email = 'admin.prueba@altokego.test';
```

## Endpoints y permisos

| Método y ruta | Acceso |
|---|---|
| `GET /` (pantalla web) y `POST /api/auth/register` | **Público** |
| `GET /api/auth/me` | Autenticado (USER o ADMIN) |
| `GET /api/pasajeros`, `GET /api/pasajeros/{id}` | Autenticado (USER o ADMIN) |
| `POST /api/pasajeros`, `PUT /api/pasajeros/{id}`, `DELETE /api/pasajeros/{id}` | Autenticado (USER o ADMIN) |
| `GET /api/viajes`, `GET /api/viajes/{id}`, `GET /api/viajes/pasajero/{pasajeroId}` | Autenticado (USER o ADMIN) |
| `POST /api/viajes`, `PUT /api/viajes/{id}` | Autenticado (USER o ADMIN) |
| `PATCH /api/viajes/{id}/estado` | **Solo ADMIN**: el operador cambia el estado (pendiente, en curso, finalizado, cancelado) |
| `DELETE /api/viajes/{id}` | **Solo ADMIN** (`ROLE_ADMIN`) |

**Decisión sobre rutas públicas:** los únicos accesos públicos son la pantalla web (solo HTML, sin datos) y el registro. No dejamos consultas públicas porque los datos de pasajeros y viajes son personales. Cualquier otra ruta requiere autenticación.

**Códigos de respuesta:**

* `201` registro o creación correctos.
* `400` datos inválidos.
* `401` sin credenciales o con credenciales incorrectas.
* `403` credenciales válidas, pero sin el rol requerido.
* `404` recurso o relación inexistente.
* `409` correo ya registrado o conflicto con datos existentes.

## Basic Auth en Postman

1. Importar `postman/AltokeGo.postman_collection.json` (`File > Import`).
2. En la pestaña **Authorization** de cada petición protegida se elige **Basic Auth**:
   * **Username:** el correo del usuario.
   * **Password:** la contraseña original, **no** el hash.
3. La colección ya trae configuradas estas variables (`Collection > Variables`): `baseUrl`, `userEmail`, `userPassword`, `adminEmail` y `adminPassword`.
4. Cada petición protegida debe enviar sus credenciales, porque la API no usa sesiones.
5. Ejecutar la colección **en orden** (`Run collection`). Antes de la carpeta de roles, el usuario administrador debe estar promovido con el `UPDATE` de arriba.

## Interfaz web y datos de demo

La aplicación incluye una interfaz web tipo app de viajes en `src/main/resources/static/index.html`, con el logo del proyecto (`logo.svg`, versión vectorial, y `logo.jpg`, el original). La sirve Spring Boot, así que no hay que instalar nada más ni configurar CORS. Al ejecutar el proyecto, abrir `http://localhost:8080`.

**Pasajero (rol USER):**
* **Pedir viaje:** elige salida y destino entre las zonas (Media Agua, Pocito, San Juan, Caucete), ve la tarifa estimada (con recargo del 15% por camino de tierra o desvío), elige la forma de pago y confirma.
* **Mis viajes:** lista de sus pedidos con el estado de cada uno.
* La primera vez completa nombre y teléfono; se guarda como pasajero con el mismo correo de su cuenta.

**Administrador u operador (rol ADMIN):**
* **Operaciones:** indicadores (viajes, pendientes, facturado y comisión del 12%), cambio de estado de cada viaje y eliminación de viajes.
* **Pasajeros:** listado y eliminación (si tiene viajes, el servidor responde 409).

Un panel desplegable al pie, **Respuesta del servidor**, muestra la última petición con su código HTTP y el JSON, útil para ver validaciones, 401 y 409 durante la demo.

**Datos de demo.** Al iniciar, `DataInitializer` carga, sin duplicar si ya existen:

| Dato | Detalle |
|---|---|
| Administrador | `admin@altokego.test` / `admin123` (rol ADMIN) |
| Pasajero | `cliente@altokego.test` / `cliente123` (rol USER) |
| Pasajeros y viajes | 4 pasajeros y 5 viajes en distintos estados |

Las contraseñas se guardan como hash BCrypt. Son credenciales ficticias para la demo local. Para desactivar la carga automática: `altokego.seed.enabled=false` en `application.properties`.

**Importante:** los pasajeros y viajes de ejemplo solo se cargan si la tabla de pasajeros está vacía. Si ya hiciste pruebas con Postman, empezá con una base limpia:

```sql
DROP DATABASE altokego_db;
CREATE DATABASE altokego_db;
```

Con estos datos ya hay un administrador, así que **no hace falta el `UPDATE` de la base** para probar el rol ADMIN en la pantalla. Las peticiones de Postman siguen funcionando con los usuarios `usuario.prueba@altokego.test` y `admin.prueba@altokego.test`.

## Alcance de la primera fase

La aplicación implementa la **Fase 1** del producto: el pasajero pide el viaje con tarifa estimada y el operador asigna el chofer en forma manual (fuera de la aplicación) y actualiza el estado. Quedan para la Fase 2: usuarios con rol de conductor y su flujo de aceptar o rechazar viajes, tabla de vehículos y ficha del chofer, cobro en línea y el canal de WhatsApp.

## Pruebas mínimas de la entrega

Todas están en la colección de Postman, con tests automáticos sobre el código de respuesta.

| Prueba | Resultado esperado |
|---|---|
| Registrar un usuario válido sin autenticación | 201, respuesta sin contraseña ni hash |
| Repetir el correo de un usuario registrado | 409, no se crea otro usuario |
| Registrar datos inválidos | 400, no se guardan |
| Ruta protegida sin credenciales o con contraseña incorrecta | 401 |
| Acceder con correo y contraseña correctos | Operación permitida |
| CRUD y consulta por relación | Funcionalidades de la primera entrega conservadas |
| Revisar el usuario guardado | Hash BCrypt en la columna `password` de `usuarios`; ninguna respuesta expone la contraseña ni el hash |
| Operación restringida con el rol correcto (ADMIN borra un viaje) | 204 |
| Credenciales válidas sin el rol requerido (USER borra un viaje) | 403, el viaje no se elimina |

Para revisar el hash en la base: `SELECT email, password, rol FROM usuarios;`. La contraseña debe empezar con `$2a$`.

## Autenticación y autorización

* **Autenticación:** comprobar quién es el usuario. Basic Auth envía correo y contraseña en cada petición, y `CustomUserDetailsService` busca al usuario por correo y Spring Security compara la contraseña con el hash BCrypt guardado. Si falla, responde **401**.
* **Autorización:** decidir qué puede hacer un usuario ya autenticado según su rol. `DELETE /api/viajes/**` exige `ROLE_ADMIN`. Si el usuario es válido pero no tiene ese rol, responde **403**.

## Entrega

* Rama: `main`.
* Commit entregado: el último commit de la rama `main` al momento de la entrega.
