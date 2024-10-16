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
-- Table structure for table `refunds`
--

DROP TABLE IF EXISTS `refunds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refunds` (
  `refund_id` int NOT NULL AUTO_INCREMENT,
  `transaction_id` int NOT NULL,
  `transaction_item_id` int NOT NULL,
  `employee_id` int NOT NULL,
  `quantity` int NOT NULL,
  `refund_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `restock` tinyint(1) DEFAULT '0',
  `amount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`refund_id`),
  KEY `transaction_id` (`transaction_id`),
  KEY `transaction_item_id` (`transaction_item_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `refunds_ibfk_1` FOREIGN KEY (`transaction_id`) REFERENCES `sales_transactions` (`transaction_id`),
  CONSTRAINT `refunds_ibfk_2` FOREIGN KEY (`transaction_item_id`) REFERENCES `transaction_items` (`transaction_item_id`),
  CONSTRAINT `refunds_ibfk_3` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refunds`
--

LOCK TABLES `refunds` WRITE;
/*!40000 ALTER TABLE `refunds` DISABLE KEYS */;
INSERT INTO `refunds` VALUES (2,57,53,1,1,'2024-06-05 02:19:21',1,0.00),(4,57,53,1,1,'2024-06-05 02:26:40',1,0.00),(5,1,1,1,1,'2024-06-05 06:09:28',0,50.00),(6,1,1,1,1,'2024-06-05 06:10:33',0,50.00),(7,1,2,1,1,'2024-06-05 06:11:56',0,50.00),(8,5,5,1,1,'2024-06-05 06:20:36',0,50.00),(9,8,11,1,1,'2024-06-05 06:21:57',0,19.99),(10,5,5,1,1,'2024-06-05 06:24:13',1,50.00),(11,5,6,1,1,'2024-06-27 09:50:10',1,50.00);
/*!40000 ALTER TABLE `refunds` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-15 23:32:13
