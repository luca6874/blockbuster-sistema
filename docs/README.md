# blockbuster-sistema
Sistema administrativo de renta y venta de videojuegos inspirado en Blockbuster.  
Proyecto desarrollado para las materias de Programación III, Interacción Humano-Máquina y Base de Datos I.

## Tecnologías
- Java
- Java Swing
- MySQL (MySQL Connector/J)
- JDBC
- Figma
- Github y Git

## Arquitectura del proyecto

El sistema sigue una arquitectura MVC (Model - View - Controller) separada en:

- `controller` → lógica y flujo del sistema
- `dao` → acceso y consultas a la base de datos
- `model` → entidades y estructuras de datos
- `view` → interfaces gráficas y paneles


## Estructura del proyecto
- /backend
- /frontend
- /database 
- /docs
- /files

## Integrantes
- Leonardo Mata Romero
- Darnell Aguilar Ramirez
- Jesus Iran Ruiz Medellin
- Luca Alexander Reinaga Genesta

## Funcionalidades implementadas
- Login y registro conectados a MySQL
- Gestión de clientes (CRUD)
- Gestión de videojuegos (CRUD)
- Gestión de rentas y compras
- Historial de operaciones
- Búsqueda y filtrado de clientes por ID
- Panel dinámico de resumen de clientes
- Perfil editable de usuario
- Validaciones y manejo de errores
- Navegación funcional entre ventanas

## Requisitos para usar el proyecto
- Tener instalado Java JDK 17 o superior. En el equipo original se probó con Java 24.
- Tener instalado Visual Studio Code o Eclipse IDE for Java Developers.
- En caso de usar VS Code, instalar la extensión:
    Extension Pack for Java de Microsoft.
- MySQL Server

## Instrucciones de ejecucion
- Descargar o clonar el proyecto.
- Abrir la carpeta completa del proyecto en VS Code, no solo frontend.
- Instalar el JDK y verificar que java y javac funcionen.
- En MySQL, crear la base de datos manualmente:(opcional de momento)
    CREATE DATABASE blockbuster;
    USE blockbuster;
- Importar el archivo: (opcional de momento)
    database/database.sql
- En VS Code, abrir frontend/src/controller/Main.java y ejecutarlo con Run Java.

