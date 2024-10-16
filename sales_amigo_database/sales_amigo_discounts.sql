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
-- Table structure for table `discounts`
--

DROP TABLE IF EXISTS `discounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discounts` (
  `discount_id` int NOT NULL AUTO_INCREMENT,
  `discount_code` varchar(255) NOT NULL,
  `discount_name` varchar(255) NOT NULL,
  `discount_percent` decimal(5,2) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `discount_type` enum('GENERAL','PRODUCT','BRAND','CATEGORY') NOT NULL,
  PRIMARY KEY (`discount_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discounts`
--

LOCK TABLES `discounts` WRITE;
/*!40000 ALTER TABLE `discounts` DISABLE KEYS */;
INSERT INTO `discounts` VALUES (1,'STORE10','',10.00,'2024-06-01','2024-06-30','GENERAL'),(2,'PROD15','Product Discount 15%',15.00,'2024-06-20','2024-06-30','PRODUCT'),(5,'BRAND20','Brand Discount 20%',30.00,'2024-07-04','2024-07-17','BRAND'),(10,'PROD15','Product Discount 15%',15.00,'2024-06-20','2024-06-30','PRODUCT'),(11,'PROD15','Product Discount 15%',15.00,'2024-06-20','2024-06-30','PRODUCT'),(12,'PROD15','Product Discount 15%',15.00,'2024-06-20','2024-06-30','GENERAL'),(20,'TET','BIG TINGS A GWAN!',50.00,'2024-06-06','2024-06-13','GENERAL'),(21,'test','vj bn',90.00,'2024-06-14','2024-06-20','PRODUCT'),(22,'test','nm',90.00,'2024-06-13','2024-06-19','PRODUCT'),(25,'jTextField1','jTextField1',40.00,'2024-06-13','2024-06-14','PRODUCT'),(26,'jTextField1','jTextField1',89.00,'2024-06-08','2024-06-08','CATEGORY'),(29,'FLASH30','Summer Sale',30.00,'2024-06-27','2024-06-28','GENERAL'),(30,'MAJOR20','Special Sale',20.00,'2024-07-03','2024-07-04','BRAND');
/*!40000 ALTER TABLE `discounts` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-15 23:32:12
