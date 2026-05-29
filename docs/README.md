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
- /frontend
- /database 
- /docs
- /files
- /frontend/src/images → imágenes de perfil de clientes
- /frontend/src/service → generación de PDF y utilidades

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
- Exportación de información de clientes en PDF
- Generación de credenciales de cliente con fotografía
- Sistema de imágenes de perfil para clientes
      Las imágenes de perfil se almacenan localmente en: frontend/src/images
- Sistema de fidelidad y acumulación de puntos

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
- En MySQL, crear la base de datos manualmente:
    CREATE DATABASE blockbuster;
    USE blockbuster;
- Importar el archivo: 
    database/database.sql
- En VS Code, abrir frontend/src/dao
      entrar a la clase ConexionBD.java y modificar password y user de ser necesario 
- En VS Code, abrir frontend/src/controller/Main.java y ejecutarlo con Run Java.

### Importar database.sql en MySQL Workbench

1. Abrir MySQL Workbench.
2. Ir a:
   `Server → Data Import`
3. Seleccionar:
   `Import from Self-Contained File`
4. Elegir el archivo:
    database/database.sql

## Configuración MySQL Connector/J

Si el proyecto no reconoce MySQL:

1. Abrir `JAVA PROJECTS`
2. Ir a `Referenced Libraries`
3. Agregar:

files/mysql-connector-j-9.3.0.jar

NOTA: En caso de haber importado una base de datos antigua, realizar nuevamente la importación.  

