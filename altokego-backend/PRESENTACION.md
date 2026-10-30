# Presentación final - AltokeGo

## 1. La idea y el problema 

* AltokeGo es una plataforma para pedir y gestionar viajes en un servicio de transporte local en Media Agua, que conecta a pasajeros con el servicio de forma ágil.
* Usuario objetivo: vecinos de Media Agua y la zona que necesitan trasladarse. La idea surgio  y qué alternativa  (por ejemplo, coordinar por mensajes o por teléfono).
* Propuesta de valor: pedir un viaje en un solo lugar, con precio y método de pago registrados, y con cuentas protegidas por usuario y contraseña.

## 2. Trabajo en equipo y metodología 

* Integrantes: Diaz Carolina, Torres Maria, Carrizo Eliana.
* División de tareas: El equipo fue quién hizo backend, base de datos, seguridad, pruebas y documentación.
* Control de versiones: Git y GitHub, con un repositorio del grupo y forks personales que se integran con pull requests.
* Tablero y reuniones: GitHub Projects, reuniones .

## 3. Arquitectura y rol de la IA 

* Tecnologías: Java 21, Spring Boot 3.2.5, Spring Data JPA, MySQL, Spring Security con Basic Auth, BCrypt, y una pantalla web en HTML y JavaScript que sirve la propia aplicación.
* Capas del backend: controller, service, repository, entity, dto, mapper y exception.
* Modelo de datos: un `Pasajero` tiene muchos `Viaje`; `Usuario` guarda las cuentas con el rol (USER o ADMIN) y la contraseña como hash.
* Uso de IA: En este proyecto se usó para revisar el código contra las consignas, generar la documentación (README), armar la colección de Postman, preparar los datos de demo y la pantalla web,. Conviene aclarar que cada integrante revisó y entiende el código que entrega.

## 4. Demostración en vivo 

Antes de empezar: MySQL encendido (con base limpia para que se carguen los datos de demo), la aplicación corriendo y `http://localhost:8080` abierto.

1. **Pantalla de inicio:** mostrar el logo y las dos formas de entrar. En **Crear cuenta**, probar una contraseña corta: el mensaje muestra el 400 con el detalle. Registrar una cuenta válida (201) y repetir el correo (409).
2. **Ingreso incorrecto:** contraseña equivocada, respuesta 401.
3. **Probar como pasajero:** pedir un viaje de Media Agua a San Juan, activar camino de tierra para ver cómo cambia la tarifa estimada y confirmar. Ir a **Mis viajes** y ver el pedido pendiente.
4. **Respuesta del servidor:** abrir el panel inferior y mostrar el 201 con el JSON, sin datos sensibles.
5. **Salir y probar como administrador:** en **Operaciones**, cambiar el estado del viaje a en curso y luego a finalizado, y ver cómo se actualizan lo facturado y la comisión del 12%. El operador asigna el chofer en forma manual, como definimos para la primera fase.
6. **Pasajeros:** intentar eliminar uno con viajes y mostrar el 409.
7. **Permisos con Postman:** `DELETE /api/viajes/{id}` con el usuario USER devuelve 403, y con el ADMIN devuelve 204. Esto distingue autenticación (quién sos) de autorización (qué podés hacer).
8. **Base de datos:** ejecutar `SELECT email, password, rol FROM usuarios;` y mostrar que la contraseña es un hash que empieza con `$2a$`.

## Cómo se conectan las materias

| Materia | Qué aporta la aplicación |
|---|---|
| Desarrollo de Aplicativos | Flujo completo de usuario, dos roles, respuestas del backend visibles, datos de demo y presentación. |
| Desarrollo Emprendedor | Pasajero que pide viaje con tarifa estimada y confirmación, recargo por camino de tierra, zonas de Media Agua y alrededores, asignación manual del chofer y comisión del 12%. Lo que queda para la Fase 2 coincide con lo que planteamos en los trabajos prácticos. |
| Lenguaje de Programación II | API con Spring Boot, JPA y MySQL, CRUD, relación pasajero-viaje, registro con BCrypt, Basic Auth y permisos por roles. |
| Bases de Datos | Tablas `usuarios`, `pasajeros` y `viajes` con claves y relación, y los mismos estados de viaje que el informe. `vehiculos` y `conductor_id` del informe se incorporan en la Fase 2. |

## 5. Obstáculos y aprendizaje 

 Ejemplos válidos de este proyecto:

* En Basic Auth se envía la contraseña original, no el hash guardado en la base.
* Diferencia entre 401 (no sabemos quién sos) y 403 (sabemos quién sos pero no tenés el rol).
* No se puede eliminar un pasajero que tiene viajes: la relación lo impide y el servicio responde 409.
* Dos registros con el mismo correo: se controla en el service y además con una restricción única en la base.

## 6. Visión a futuro

* **Producción:** desplegar en un servicio en la nube, pasar las credenciales de la base a variables de entorno, usar HTTPS y desactivar los datos de demo (`altokego.seed.enabled=false`).
* **Seguridad:** reemplazar Basic Auth por tokens (JWT) y manejar los roles desde un administrador.
* **Dominio:** agregar conductores y vehículos, asignar conductor a cada viaje y manejar estados del viaje (pendiente, en curso, finalizado).
* **Mejoras de datos:** usar `DECIMAL` para el dinero y estados con valores fijos (ENUM).
* **Funciones futuras:** pago con MercadoPago, notificaciones en tiempo real y versión móvil o PWA.

## Lista de chequeo del día

1. Base de datos iniciada, aplicación corriendo y pantalla cargada antes de empezar.
2. Datos de demo cargados (se hace solo al arrancar).
