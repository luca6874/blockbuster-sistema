-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: blockbuster
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `primer_apellido` varchar(50) NOT NULL,
  `segundo_apellido` varchar(50) DEFAULT NULL,
  `correo_electronico` varchar(100) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `telefono` varchar(10) DEFAULT NULL,
  `lvl_fidelidad` int DEFAULT '0',
  `puntos` int DEFAULT '0',
  `foto` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `correo_electronico` (`correo_electronico`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (2,'Jesus Iran','Ruiz','Medellin','soimediocre@hotmail.com','2026-05-09','1122334455',1,46,NULL),(3,'Darnell sueño','Aguilar','Ramirez','wasa@sueñomail.com','2001-09-11','1234567890',2,580,'Captura_de_pantalla__11__1.png'),(4,'leonardazo','pichard','colon','LeonardoChickcrAzy@hotmail.com','2001-09-11','6122333612',1,0,NULL),(7,'ya','ayaya','ayayaa','yayyaya@mail.com','2001-09-11','1234509876',1,34,NULL),(8,'Iran','leonardo','darnell','asasa@hj.com','2001-09-11','0987654312',1,0,'iran.jpg');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operaciones`
--

DROP TABLE IF EXISTS `operaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operaciones` (
  `id_operacion` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int DEFAULT NULL,
  `id_videojuego` int DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  `tipo` enum('RENTA','COMPRA') NOT NULL,
  `monto` decimal(8,2) NOT NULL,
  `descuento` decimal(5,2) DEFAULT '0.00',
  `fecha_operacion` date NOT NULL,
  `fecha_devolucion` date DEFAULT NULL,
  `devuelto` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_operacion`),
  KEY `id_cliente` (`id_cliente`),
  KEY `id_videojuego` (`id_videojuego`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `operaciones_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`),
  CONSTRAINT `operaciones_ibfk_2` FOREIGN KEY (`id_videojuego`) REFERENCES `videojuegos` (`id_videojuego`),
  CONSTRAINT `operaciones_ibfk_3` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operaciones`
--

LOCK TABLES `operaciones` WRITE;
/*!40000 ALTER TABLE `operaciones` DISABLE KEYS */;
INSERT INTO `operaciones` VALUES (1,3,3,2,'COMPRA',111.00,0.00,'2026-05-18',NULL,0),(2,3,3,2,'COMPRA',111.00,0.00,'2026-05-18',NULL,0),(3,3,1,2,'COMPRA',343.00,0.00,'2026-05-18',NULL,0),(4,3,1,2,'COMPRA',343.00,0.00,'2026-05-19',NULL,0),(5,3,2,2,'RENTA',123.00,0.00,'2026-05-19','2026-11-11',0),(6,7,1,2,'COMPRA',343.00,0.00,'2026-05-19',NULL,0),(7,3,1,2,'COMPRA',343.00,0.00,'2026-05-24',NULL,0),(8,3,1,2,'COMPRA',343.00,0.00,'2026-05-24',NULL,0),(9,3,1,2,'COMPRA',343.00,0.00,'2026-05-24',NULL,0),(10,3,1,2,'COMPRA',343.00,0.00,'2026-05-24',NULL,0),(11,3,1,2,'COMPRA',343.00,0.00,'2026-05-24',NULL,0),(12,3,1,2,'COMPRA',343.00,0.00,'2026-05-24',NULL,0),(13,3,1,2,'COMPRA',343.00,0.00,'2026-05-24',NULL,0),(14,3,1,2,'COMPRA',343.00,0.00,'2026-05-24',NULL,0),(15,3,1,2,'COMPRA',343.00,0.00,'2026-05-24',NULL,0),(16,3,2,2,'COMPRA',321.00,0.00,'2026-05-24',NULL,0),(17,3,2,2,'COMPRA',321.00,0.00,'2026-05-24',NULL,0),(18,3,1,2,'COMPRA',343.00,0.00,'2026-05-24',NULL,0),(19,3,2,2,'COMPRA',321.00,0.00,'2026-05-24',NULL,0),(20,2,1,2,'COMPRA',343.00,0.00,'2026-05-24',NULL,0),(21,2,2,2,'RENTA',123.00,0.00,'2026-05-26','2026-05-29',1),(22,3,1,2,'COMPRA',343.00,17.15,'2026-05-26',NULL,0),(23,3,1,2,'COMPRA',343.00,17.15,'2026-05-28',NULL,0);
/*!40000 ALTER TABLE `operaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `primer_apellido` varchar(50) DEFAULT NULL,
  `segundo_apellido` varchar(50) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `correo` varchar(100) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,NULL,NULL,NULL,'asdfgj','waltuh@mail.com',NULL,'1234'),(2,'Luca Alexander','Reinaga','Genesta','luca','lucapro@mail.com','2006-06-29','1234'),(3,NULL,NULL,NULL,'DArnel','atun@frijolmail.com',NULL,'1234'),(4,NULL,NULL,NULL,'5','leonardo@jotmail.com',NULL,'123456'),(6,'Mata','manzana','escritorio','leo@jotmail.com','leo@jotmail.com','2001-09-11','123456'),(7,'Iran','ruiz','medellin','chopon','ruiz@jotmail','2001-09-11','1234567'),(8,'olaaaa','was','waaa','juanjo','juan123@mail.com','2025-06-12','1234567890');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `videojuegos`
--

DROP TABLE IF EXISTS `videojuegos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videojuegos` (
  `id_videojuego` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `plataforma` varchar(45) DEFAULT NULL,
  `genero` varchar(50) DEFAULT NULL,
  `clasificacion` enum('E','T','M') DEFAULT NULL,
  `anio_lanzamiento` year DEFAULT NULL,
  `precio_renta` decimal(6,2) DEFAULT NULL,
  `precio_compra` decimal(6,2) DEFAULT NULL,
  `stock` int NOT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `puntos` int DEFAULT '0',
  `activo` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id_videojuego`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videojuegos`
--

LOCK TABLES `videojuegos` WRITE;
/*!40000 ALTER TABLE `videojuegos` DISABLE KEYS */;
INSERT INTO `videojuegos` VALUES (1,'Halo 3 ODST','Xbox 360','FPS','M',2009,117.00,343.00,51,'caratulaGame3.png',77,1),(2,'The Evil Within','Xbox','Terror','E',2014,123.00,321.00,10,'caratulaGame5.png',10,1),(3,'fsdffsfsf','ffsffs','fsfsfs','E',2000,111.00,111.00,9,NULL,11,0);
/*!40000 ALTER TABLE `videojuegos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-28  1:37:19
