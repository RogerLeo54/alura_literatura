# 📚 Proyecto: Literatura - Alura Latam

Este es un proyecto del curso de Java con Spring Boot de **Alura Latam**, que permite consultar libros mediante la API de [Gutendex](https://gutendex.com/) y almacenar los datos en una base de datos usando JPA (Hibernate).

---

## 🔧 Tecnologías utilizadas

- Java 17
- Spring Boot
- Maven
- JPA + Hibernate
- PostgreSQL (o la base de datos que configures)
- API Gutendex (Project Gutenberg)
- IntelliJ IDEA
- Dotenv

---

## 🚀 Funcionalidades

Desde el menú principal puedes:

1. 🔍 Buscar libro por título
2. 📖 Listar libros registrados
3. ✍️ Listar autores registrados
4. ⏳ Listar autores vivos en determinado año
5. 🌐 Listar libros por idioma
0. ❌ Salir del sistema

---

## 🧪 Ejemplo de uso

Al iniciar la aplicación, verás un menú interactivo en consola. Puedes buscar un libro (ej. `"Pride and Prejudice"`), y si no está en la base de datos, lo descargará de la API y lo guardará junto con su autor.

---

## 📁 Estructura del proyecto
## 📁 Estructura del proyecto

```plaintext
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── alura/
│   │           └── literatura/
│   │               ├── dto/
│   │               ├── model/
│   │               ├── repository/
│   │               ├── service/
│   │               └── LiteraturaApplication.java
│   └── resources/
│       └── application.properties
```
---

## ✅ Requisitos previos

- Tener instalado Java 17
- Configurar tu base de datos (PostgreSQL, MySQL, etc.)
- Crear un archivo `.env` con las variables de entorno:

```env
DB_HOST=localhost
DB_NAME=literatura
DB_USER=tu_usuario
DB_PASSWORD=tu_contraseña

