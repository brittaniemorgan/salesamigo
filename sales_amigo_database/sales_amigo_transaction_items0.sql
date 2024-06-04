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
-- Table structure for table `transaction_items`
--

DROP TABLE IF EXISTS `transaction_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_items` (
  `transaction_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `discount_id` int DEFAULT NULL,
  KEY `transaction_id` (`transaction_id`),
  KEY `transaction_items_ibfk_2` (`product_id`),
  KEY `fk_discount_id` (`discount_id`),
  CONSTRAINT `fk_discount_id` FOREIGN KEY (`discount_id`) REFERENCES `discounts` (`discount_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `transaction_items_ibfk_1` FOREIGN KEY (`transaction_id`) REFERENCES `sales_transactions` (`transaction_id`),
  CONSTRAINT `transaction_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `productvariant` (`variant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_items`
--

LOCK TABLES `transaction_items` WRITE;
/*!40000 ALTER TABLE `transaction_items` DISABLE KEYS */;
INSERT INTO `transaction_items` VALUES (1,1,2,50.00,NULL),(1,2,1,50.00,NULL),(2,1,2,50.00,NULL),(2,2,1,50.00,NULL),(5,1,2,50.00,NULL),(5,2,1,50.00,NULL),(6,1,2,50.00,NULL),(6,2,1,50.00,NULL),(7,1,2,50.00,NULL),(7,2,1,50.00,NULL),(8,1,1,19.99,NULL),(9,1,1,19.99,NULL),(10,1,1,19.99,NULL),(13,1,2,50.00,NULL),(13,2,1,50.00,NULL),(14,1,2,50.00,NULL),(14,2,1,50.00,NULL),(24,1,2,50.00,2),(25,1,2,50.00,1),(26,2,2,50.00,2),(1,1,2,10.00,1),(1,1,2,10.00,2),(31,2,1,15.00,2),(1,1,2,10.00,NULL),(32,3,1,15.00,NULL),(33,2,1,19.99,2),(34,2,1,19.99,2),(35,2,2,50.00,2),(36,3,1,49.99,1),(36,1,1,19.99,1),(36,2,1,19.99,2),(37,2,1,17.99,1),(38,3,1,17.99,1),(39,3,1,44.99,1),(40,3,1,44.99,1),(41,3,1,44.99,1),(42,3,1,44.99,1),(43,3,1,44.99,1),(44,3,1,44.99,1),(45,3,1,45.00,1),(46,4,1,500.00,1),(47,4,1,500.00,1),(48,4,1,500.00,1),(49,4,1,500.00,1),(50,4,1,500.00,1),(51,4,1,44.99,1),(51,5,1,134.99,1),(52,5,1,134.99,1),(53,5,1,134.99,1),(54,5,1,134.99,1),(55,5,1,134.99,1),(56,6,1,149.99,NULL),(57,6,2,299.98,NULL),(58,7,1,89.99,NULL),(59,7,1,80.99,1);
/*!40000 ALTER TABLE `transaction_items` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-04 17:07:04
