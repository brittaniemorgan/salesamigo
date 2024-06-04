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
-- Table structure for table `sales_transactions`
--

DROP TABLE IF EXISTS `sales_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_transactions` (
  `transaction_id` int NOT NULL AUTO_INCREMENT,
  `transaction_date` datetime DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `customer_id` int DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `discount_id` int DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `employee_id` (`employee_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `sales_transactions_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`),
  CONSTRAINT `sales_transactions_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_transactions`
--

LOCK TABLES `sales_transactions` WRITE;
/*!40000 ALTER TABLE `sales_transactions` DISABLE KEYS */;
INSERT INTO `sales_transactions` VALUES (1,'2024-05-27 21:05:05',2,1,150.00,'Credit Card',NULL),(2,'2024-05-27 21:16:17',2,1,150.00,'Credit Card',NULL),(5,'2024-05-27 21:19:17',2,1,150.00,'Credit Card',NULL),(6,'2024-05-27 21:19:34',2,1,150.00,'Credit Card',NULL),(7,'2024-05-27 21:20:03',2,1,150.00,'Credit Card',NULL),(8,'2024-05-29 01:57:01',1,1,0.00,'Cash',NULL),(9,'2024-05-29 01:58:49',1,1,0.00,'Cash',NULL),(10,'2024-05-29 02:02:39',1,1,0.00,'Cash',NULL),(13,'2024-06-02 12:08:08',1,1,150.00,'Credit Card',NULL),(14,'2024-06-02 12:08:20',1,1,150.00,'Credit Card',NULL),(24,'2024-06-02 12:18:19',1,1,150.00,'Credit Card',NULL),(25,'2024-06-02 12:18:40',1,1,150.00,'Credit Card',NULL),(26,'2024-06-02 12:22:50',1,1,150.00,'Credit Card',NULL),(31,'2024-06-02 12:46:11',2,1,35.00,'Credit Card',NULL),(32,'2024-06-02 12:52:47',2,1,35.00,'Credit Card',NULL),(33,'2024-06-02 15:48:08',1,1,0.00,'Cash',NULL),(34,'2024-06-02 15:49:01',1,1,0.00,'Cash',NULL),(35,'2024-06-02 17:53:01',1,1,150.00,'Credit Card',NULL),(36,'2024-06-02 17:54:38',1,1,76.00,'Cash',NULL),(37,'2024-06-04 12:17:04',1,1,16.00,'Cash',NULL),(38,'2024-06-04 12:19:40',1,1,16.00,'Cash',NULL),(39,'2024-06-04 12:21:41',1,1,43.00,'Cash',NULL),(40,'2024-06-04 12:22:53',1,1,43.00,'Cash',NULL),(41,'2024-06-04 12:23:01',1,4,43.00,'Cash',NULL),(42,'2024-06-04 12:24:11',1,4,43.00,'Cash',NULL),(43,'2024-06-04 12:25:22',1,4,43.00,'Cash',NULL),(44,'2024-06-04 12:26:22',1,4,43.00,'Cash',NULL),(45,'2024-06-04 12:27:27',1,4,43.00,'Cash',NULL),(46,'2024-06-04 12:28:09',1,4,43.00,'Cash',NULL),(47,'2024-06-04 12:28:25',1,4,43.00,'Cash',NULL),(48,'2024-06-04 12:31:21',1,3,500.00,'Cash',NULL),(49,'2024-06-04 12:32:36',1,4,43.00,'Cash',NULL),(50,'2024-06-04 12:33:02',1,2,100.00,'Cash',NULL),(51,'2024-06-04 12:35:40',1,1,178.00,'Cash',NULL),(52,'2024-06-04 12:48:33',1,1,134.00,'Cash',NULL),(53,'2024-06-04 12:53:06',1,1,133.00,'Cash',NULL),(54,'2024-06-04 12:55:39',1,2,133.00,'Cash',NULL),(55,'2024-06-04 13:00:54',1,2,133.00,'Cash',NULL),(56,'2024-06-04 13:15:29',1,1,298.00,'Cash',NULL),(57,'2024-06-04 13:15:39',1,1,299.00,'Cash',NULL),(58,'2024-06-04 13:52:14',1,1,89.00,'Credit Card',NULL),(59,'2024-06-04 15:02:22',1,2,80.00,'Bank Transfer',NULL);
/*!40000 ALTER TABLE `sales_transactions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-04 17:07:03
