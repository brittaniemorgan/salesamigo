-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: sales_amigo
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `restock`
--

DROP TABLE IF EXISTS `restock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restock` (
  `restock_id` int NOT NULL AUTO_INCREMENT,
  `variant_id` int DEFAULT NULL,
  `restock_date` datetime DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`restock_id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `restock_ibfk_1` FOREIGN KEY (`variant_id`) REFERENCES `productvariant` (`variant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restock`
--

LOCK TABLES `restock` WRITE;
/*!40000 ALTER TABLE `restock` DISABLE KEYS */;
INSERT INTO `restock` VALUES (1,1,'2024-05-01 00:00:00',50,1039.96),(2,2,'2024-05-01 00:00:00',50,1039.96),(3,3,'2024-05-01 00:00:00',50,642.96),(4,4,'2024-05-01 00:00:00',50,642.96),(5,6,'2024-05-01 00:00:00',50,397.92),(6,5,'2024-05-01 00:00:00',50,397.92),(7,7,'2024-05-01 00:00:00',50,2544.99),(8,8,'2024-05-01 00:00:00',50,2544.99),(9,9,'2024-05-01 00:00:00',50,674.95),(10,10,'2024-05-01 00:00:00',50,674.95),(11,11,'2024-05-01 00:00:00',50,749.95),(12,12,'2024-05-01 00:00:00',50,749.95),(13,14,'2024-05-01 00:00:00',50,170.98),(14,22,'2024-05-01 00:00:00',50,170.98),(15,13,'2024-05-01 00:00:00',50,170.98),(16,15,'2024-05-01 00:00:00',50,89.99),(17,16,'2024-05-01 00:00:00',50,89.99);
/*!40000 ALTER TABLE `restock` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-25  0:56:01