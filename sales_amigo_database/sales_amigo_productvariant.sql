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
-- Table structure for table `productvariant`
--

DROP TABLE IF EXISTS `productvariant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productvariant` (
  `variant_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `size_id` int NOT NULL,
  `color` varchar(50) NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `min_quantity` int NOT NULL,
  `date_added` date NOT NULL DEFAULT (curdate()),
  PRIMARY KEY (`variant_id`),
  UNIQUE KEY `product_id` (`product_id`,`size_id`,`color`),
  KEY `size_id` (`size_id`),
  CONSTRAINT `productvariant_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  CONSTRAINT `productvariant_ibfk_2` FOREIGN KEY (`size_id`) REFERENCES `size` (`size_id`),
  CONSTRAINT `productvariant_chk_1` CHECK ((`quantity` >= 0)),
  CONSTRAINT `productvariant_chk_2` CHECK ((`price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productvariant`
--

LOCK TABLES `productvariant` WRITE;
/*!40000 ALTER TABLE `productvariant` DISABLE KEYS */;
INSERT INTO `productvariant` VALUES (1,1,1,'White',20,19.99,10,'2024-05-26'),(2,1,2,'Black',23,19.99,5,'2024-05-26'),(3,2,2,'Blue',34,49.99,3,'2024-05-26'),(4,2,3,'Black',34,49.99,3,'2024-05-26'),(5,3,3,'Red',35,149.99,2,'2024-05-26'),(6,3,1,'Black',40,149.99,2,'2024-05-26'),(7,4,1,'Gray',44,89.99,2,'2024-05-26'),(8,4,2,'White',44,89.99,2,'2024-05-26'),(9,5,1,'Black',45,199.99,1,'2024-05-26'),(10,5,2,'Brown',45,199.99,5,'2024-05-26'),(11,6,2,'Red',47,129.99,4,'2024-05-26'),(12,6,3,'Black',47,129.99,4,'2024-05-26'),(13,7,3,'Gray',48,79.99,5,'2024-05-26'),(14,7,1,'Blue',48,79.99,5,'2024-05-26'),(15,8,1,'Black',49,59.99,2,'2024-05-26'),(16,8,2,'White',49,59.99,2,'2024-05-26'),(22,7,2,'Blue',48,90.99,5,'2024-06-05'),(24,6,2,'pink',47,129.99,4,'2024-06-24');
/*!40000 ALTER TABLE `productvariant` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-15 23:32:14
