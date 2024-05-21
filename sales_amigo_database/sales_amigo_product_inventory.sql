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
-- Table structure for table `product_inventory`
--

DROP TABLE IF EXISTS `product_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_inventory` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `price` decimal(10,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `maximum_quantity` int DEFAULT NULL,
  `minimum_quantity` int DEFAULT NULL,
  `date_added` date DEFAULT NULL,
  `last_date_updated` timestamp NULL DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_inventory`
--

LOCK TABLES `product_inventory` WRITE;
/*!40000 ALTER TABLE `product_inventory` DISABLE KEYS */;
INSERT INTO `product_inventory` VALUES (1,'jTextField2','Updated product description.',12.99,50,NULL,10,'2024-05-21','2024-05-21 08:35:55','Item 1'),(2,'Sample Product','A sample product description.',299.99,50,NULL,10,'2024-05-21','2024-05-21 08:59:44','Electronics'),(3,'Sample Product','A sample product description.',299.99,50,NULL,10,'2024-05-21','2024-05-21 08:59:55','Electronics'),(4,'Sample Product','A sample product description.',299.99,50,NULL,10,'2024-05-21','2024-05-21 09:01:51','Electronics'),(5,'jTextField2','A sample product description.',299.99,50,NULL,10,'2024-05-21','2024-05-21 09:08:56','Item 1'),(6,'jTextField2','description',82.00,10,NULL,5,'2024-05-21','2024-05-21 09:31:57','Item 1'),(7,'ghjk','ghjk vbhjk',10.00,2,NULL,2,'2024-05-21','2024-05-21 17:31:48','Item 1'),(8,'jTextField2','jTextField2',1.00,6,NULL,9,'2024-05-21','2024-05-21 17:32:22','cat'),(9,'jTextField2','jTextField2',1.00,1,NULL,1,'2024-05-21','2024-05-21 17:39:31','Item 2'),(10,'jTextField2','new desc',1.00,2,NULL,1,'2024-05-21','2024-05-21 17:44:21','Item 3'),(11,'jTextField2','jTextField2',1.00,1,NULL,1,'2024-05-21','2024-05-21 17:46:16','Item 1');
/*!40000 ALTER TABLE `product_inventory` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-21 14:23:59
