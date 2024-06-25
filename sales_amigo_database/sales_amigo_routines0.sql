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
-- Temporary view structure for view `ending_inventory_view`
--

DROP TABLE IF EXISTS `ending_inventory_view`;
/*!50001 DROP VIEW IF EXISTS `ending_inventory_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `ending_inventory_view` AS SELECT 
 1 AS `product_id`,
 1 AS `variant_id`,
 1 AS `ending_inventory`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `monthly_sales_view`
--

DROP TABLE IF EXISTS `monthly_sales_view`;
/*!50001 DROP VIEW IF EXISTS `monthly_sales_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `monthly_sales_view` AS SELECT 
 1 AS `sale_year`,
 1 AS `sale_month`,
 1 AS `product_id`,
 1 AS `product_name`,
 1 AS `variant_id`,
 1 AS `size_id`,
 1 AS `color`,
 1 AS `total_quantity_sold`,
 1 AS `total_sales`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `daily_sales_view`
--

DROP TABLE IF EXISTS `daily_sales_view`;
/*!50001 DROP VIEW IF EXISTS `daily_sales_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `daily_sales_view` AS SELECT 
 1 AS `sale_date`,
 1 AS `product_id`,
 1 AS `product_name`,
 1 AS `variant_id`,
 1 AS `size_id`,
 1 AS `color`,
 1 AS `total_quantity_sold`,
 1 AS `total_sales`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `beginning_inventory_view`
--

DROP TABLE IF EXISTS `beginning_inventory_view`;
/*!50001 DROP VIEW IF EXISTS `beginning_inventory_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `beginning_inventory_view` AS SELECT 
 1 AS `variant_id`,
 1 AS `product_id`,
 1 AS `beginning_inventory`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `weekly_sales_view`
--

DROP TABLE IF EXISTS `weekly_sales_view`;
/*!50001 DROP VIEW IF EXISTS `weekly_sales_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `weekly_sales_view` AS SELECT 
 1 AS `sale_year`,
 1 AS `sale_week`,
 1 AS `product_id`,
 1 AS `product_name`,
 1 AS `variant_id`,
 1 AS `size_id`,
 1 AS `color`,
 1 AS `total_quantity_sold`,
 1 AS `total_sales`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `yearly_sales_view`
--

DROP TABLE IF EXISTS `yearly_sales_view`;
/*!50001 DROP VIEW IF EXISTS `yearly_sales_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `yearly_sales_view` AS SELECT 
 1 AS `sale_year`,
 1 AS `product_id`,
 1 AS `product_name`,
 1 AS `variant_id`,
 1 AS `size_id`,
 1 AS `color`,
 1 AS `total_quantity_sold`,
 1 AS `total_sales`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `inventory_metrics_view`
--

DROP TABLE IF EXISTS `inventory_metrics_view`;
/*!50001 DROP VIEW IF EXISTS `inventory_metrics_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `inventory_metrics_view` AS SELECT 
 1 AS `product_id`,
 1 AS `variant_id`,
 1 AS `avg_inventory_level`,
 1 AS `turnover_rate`,
 1 AS `sell_through_rate`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `ending_inventory_view`
--

/*!50001 DROP VIEW IF EXISTS `ending_inventory_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`swen3920`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `ending_inventory_view` AS select `pv`.`product_id` AS `product_id`,`pv`.`variant_id` AS `variant_id`,sum(`pv`.`quantity`) AS `ending_inventory` from `productvariant` `pv` where (`pv`.`date_added` <= '2024-06-24') group by `pv`.`product_id`,`pv`.`variant_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `monthly_sales_view`
--

/*!50001 DROP VIEW IF EXISTS `monthly_sales_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`swen3920`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `monthly_sales_view` AS select year(`st`.`transaction_date`) AS `sale_year`,month(`st`.`transaction_date`) AS `sale_month`,`p`.`product_id` AS `product_id`,`p`.`name` AS `product_name`,`pv`.`variant_id` AS `variant_id`,`pv`.`size_id` AS `size_id`,`pv`.`color` AS `color`,coalesce(sum(`ti`.`quantity`),0) AS `total_quantity_sold`,coalesce(sum((`ti`.`quantity` * `ti`.`price`)),0) AS `total_sales` from (((`sales_transactions` `st` join `transaction_items` `ti` on((`st`.`transaction_id` = `ti`.`transaction_id`))) join `productvariant` `pv` on((`ti`.`product_id` = `pv`.`variant_id`))) join `product` `p` on((`pv`.`product_id` = `p`.`product_id`))) where (`st`.`transaction_date` between '2024-05-01' and '2024-06-24') group by `sale_year`,`sale_month`,`p`.`product_id`,`pv`.`variant_id`,`pv`.`size_id`,`pv`.`color` order by `sale_year`,`sale_month` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `daily_sales_view`
--

/*!50001 DROP VIEW IF EXISTS `daily_sales_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`swen3920`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `daily_sales_view` AS select cast(`st`.`transaction_date` as date) AS `sale_date`,`p`.`product_id` AS `product_id`,`p`.`name` AS `product_name`,`pv`.`variant_id` AS `variant_id`,`pv`.`size_id` AS `size_id`,`pv`.`color` AS `color`,coalesce(sum(`ti`.`quantity`),0) AS `total_quantity_sold`,coalesce(sum((`ti`.`quantity` * `ti`.`price`)),0) AS `total_sales` from (((`sales_transactions` `st` join `transaction_items` `ti` on((`st`.`transaction_id` = `ti`.`transaction_id`))) join `productvariant` `pv` on((`ti`.`product_id` = `pv`.`variant_id`))) join `product` `p` on((`pv`.`product_id` = `p`.`product_id`))) where (`st`.`transaction_date` between '2024-05-01' and '2024-06-24') group by `sale_date`,`p`.`product_id`,`pv`.`variant_id`,`pv`.`size_id`,`pv`.`color` order by `sale_date` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `beginning_inventory_view`
--

/*!50001 DROP VIEW IF EXISTS `beginning_inventory_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`swen3920`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `beginning_inventory_view` AS select `r`.`variant_id` AS `variant_id`,`pv`.`variant_id` AS `product_id`,(sum(`r`.`quantity`) - coalesce(sum(`ti`.`quantity`),0)) AS `beginning_inventory` from ((`restock` `r` left join (select `ti`.`product_id` AS `product_id`,`pv`.`variant_id` AS `variant_id`,sum(`ti`.`quantity`) AS `quantity` from ((`transaction_items` `ti` join `productvariant` `pv` on((`ti`.`product_id` = `pv`.`variant_id`))) join `sales_transactions` `st` on((`ti`.`transaction_id` = `st`.`transaction_id`))) where (`st`.`transaction_date` < '2024-05-01') group by `ti`.`product_id`,`pv`.`variant_id`) `ti` on(((`r`.`variant_id` = `ti`.`product_id`) and (`r`.`variant_id` = `ti`.`variant_id`)))) join `productvariant` `pv` on((`r`.`variant_id` = `pv`.`variant_id`))) where (`r`.`restock_date` < '2024-05-01') group by `r`.`variant_id`,`pv`.`variant_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `weekly_sales_view`
--

/*!50001 DROP VIEW IF EXISTS `weekly_sales_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`swen3920`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `weekly_sales_view` AS select year(`st`.`transaction_date`) AS `sale_year`,week(`st`.`transaction_date`,0) AS `sale_week`,`p`.`product_id` AS `product_id`,`p`.`name` AS `product_name`,`pv`.`variant_id` AS `variant_id`,`pv`.`size_id` AS `size_id`,`pv`.`color` AS `color`,coalesce(sum(`ti`.`quantity`),0) AS `total_quantity_sold`,coalesce(sum((`ti`.`quantity` * `ti`.`price`)),0) AS `total_sales` from (((`sales_transactions` `st` join `transaction_items` `ti` on((`st`.`transaction_id` = `ti`.`transaction_id`))) join `productvariant` `pv` on((`ti`.`product_id` = `pv`.`variant_id`))) join `product` `p` on((`pv`.`product_id` = `p`.`product_id`))) where (`st`.`transaction_date` between '2024-05-01' and '2024-06-24') group by `sale_year`,`sale_week`,`p`.`product_id`,`pv`.`variant_id`,`pv`.`size_id`,`pv`.`color` order by `sale_year`,`sale_week` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `yearly_sales_view`
--

/*!50001 DROP VIEW IF EXISTS `yearly_sales_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`swen3920`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `yearly_sales_view` AS select year(`st`.`transaction_date`) AS `sale_year`,`p`.`product_id` AS `product_id`,`p`.`name` AS `product_name`,`pv`.`variant_id` AS `variant_id`,`pv`.`size_id` AS `size_id`,`pv`.`color` AS `color`,coalesce(sum(`ti`.`quantity`),0) AS `total_quantity_sold`,coalesce(sum((`ti`.`quantity` * `ti`.`price`)),0) AS `total_sales` from (((`sales_transactions` `st` join `transaction_items` `ti` on((`st`.`transaction_id` = `ti`.`transaction_id`))) join `productvariant` `pv` on((`ti`.`product_id` = `pv`.`variant_id`))) join `product` `p` on((`pv`.`product_id` = `p`.`product_id`))) where (`st`.`transaction_date` between '2024-05-01' and '2024-06-24') group by `sale_year`,`p`.`product_id`,`pv`.`variant_id`,`pv`.`size_id`,`pv`.`color` order by `sale_year` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `inventory_metrics_view`
--

/*!50001 DROP VIEW IF EXISTS `inventory_metrics_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`swen3920`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `inventory_metrics_view` AS select `bi`.`product_id` AS `product_id`,`bi`.`variant_id` AS `variant_id`,((`bi`.`beginning_inventory` + `ei`.`ending_inventory`) / 2) AS `avg_inventory_level`,((coalesce(sum(`ds`.`total_quantity_sold`),0) / ((`bi`.`beginning_inventory` + `ei`.`ending_inventory`) / 2)) * 100) AS `turnover_rate`,((coalesce(sum(`ds`.`total_quantity_sold`),0) / `bi`.`beginning_inventory`) * 100) AS `sell_through_rate` from ((`beginning_inventory_view` `bi` join `ending_inventory_view` `ei` on((`bi`.`variant_id` = `ei`.`variant_id`))) left join `daily_sales_view` `ds` on((`bi`.`variant_id` = `ds`.`variant_id`))) group by `bi`.`product_id`,`bi`.`variant_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-25  0:56:01
