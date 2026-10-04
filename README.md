# ⚙️ Backend - API REST Tienda de Lavadoras

API RESTful desarrollada en **Java 17** y **Spring Boot 3** para la gestión de productos, usuarios y pedidos.

## 🚀 Tecnologías
- Java 17 / Spring Boot 3
- Spring Security + JWT
- Spring Data JPA + Hibernate
- Base de Datos MySQL (XAMPP)
- Pruebas Unitarias: JUnit 5 & Mockito

## 🛠️ Ejecución Local
1. Iniciar los servicios de MySQL en **XAMPP Control Panel**.
2. Crear la base de datos `tienda_lavadoras` en phpMyAdmin.
3. Configurar credenciales en `src/main/resources/application.properties`.
4. Ejecutar la aplicación en la terminal:
   ```bash
   ./mvnw spring-boot:run