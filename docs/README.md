# blockbuster-sistema
Sistema de renta y venta de videojuegos tipo Blockbuster - Proyecto Programación 3
## Tecnologías
- Java
- MySQL (MySQL Connector/J)
- JDBC
- Figma
- Github y Git

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

## Funcionalidades
- Gestión de clientes
- Gestión de videojuegos
- Rentas y compras

## Descripcion del proyecto
- Proyecto desarrollado para las materias: Programacion III, Interacción Humano-maquina y Base de datos I
- Navegacion entre ventanas funcional

## Requisitos para usar el proyecto
- Tener instalado Java JDK 17 o superior. En el equipo original se probó con Java 24.
- Tener instalado Visual Studio Code o Eclipse IDE for Java Developers.
- En caso de usar VS Code, instalar la extensión:
    Extension Pack for Java de Microsoft.

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
  

