-- MariaDB dump 10.17  Distrib 10.4.8-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: trabalho_poo
-- ------------------------------------------------------
-- Server version	10.4.8-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categoria` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (26,'Pizza'),(27,'Hamburger'),(28,'Alaminuta'),(29,'Pastel'),(30,'Massa'),(31,'Salgado'),(32,'Sanduíche'),(34,'Empada'),(35,'Panqueca'),(36,'Torta Salgada'),(37,'Torta Doce'),(38,'Coxinha'),(40,'Rocambole'),(41,'Torrada');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cliente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1,'teste'),(2,'teste2'),(3,'teste3'),(4,'teste4'),(5,'teste5'),(6,'testando'),(7,'Luiz Alles'),(8,'Arthur Santos');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedido` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cliente_id` int(11) NOT NULL,
  `data_pedido` datetime NOT NULL,
  `finalizado` tinyint(1) NOT NULL DEFAULT 0,
  `entregue` tinyint(1) NOT NULL DEFAULT 0,
  `valor_total` decimal(6,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id`),
  KEY `cliente_id_idx` (`cliente_id`),
  CONSTRAINT `cliente_id` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
INSERT INTO `pedido` VALUES (86,7,'2019-12-01 21:12:56',1,0,215.10),(87,7,'2019-12-01 21:14:20',1,0,152.90),(88,8,'2019-12-01 21:16:58',1,1,898.95),(89,7,'2019-12-01 21:18:07',1,0,104.00),(90,7,'2019-12-01 21:18:46',0,0,0.00),(91,8,'2019-12-01 21:19:22',1,1,131.50),(92,8,'2019-12-01 21:21:01',0,0,0.00),(93,7,'2019-12-01 21:22:27',1,0,126.00),(94,8,'2019-12-01 21:22:28',1,1,125.25),(95,8,'2019-12-01 21:23:29',1,1,6.00),(96,8,'2019-12-01 21:24:08',1,0,2711.50),(97,7,'2019-12-01 21:24:24',1,1,22.00),(98,8,'2019-12-01 21:25:39',1,0,603.00),(99,7,'2019-12-01 21:26:21',1,0,74.80),(100,7,'2019-12-01 21:42:19',1,1,7.90),(101,7,'2019-12-01 21:43:34',1,1,60.00),(102,7,'2019-12-01 21:51:30',1,1,26.30),(103,7,'2019-12-01 22:55:38',1,1,53.40),(104,7,'2019-12-01 22:58:55',1,1,67.00),(105,7,'2019-12-01 23:00:59',1,1,26.30),(106,7,'2019-12-01 23:03:49',1,0,26.70),(107,7,'2019-12-01 23:04:28',1,1,26.70),(108,7,'2019-12-02 01:28:33',1,0,60.00),(109,7,'2019-12-02 01:31:53',1,0,122.50),(110,8,'2019-12-02 01:33:13',1,0,66.00),(111,7,'2019-12-02 01:35:21',1,0,135.50),(112,7,'2019-12-02 01:36:37',1,0,215.50),(113,8,'2019-12-02 01:37:48',1,0,202.50),(114,8,'2019-12-02 01:39:00',1,0,192.00),(115,8,'2019-12-02 01:40:07',1,0,3450.00),(116,8,'2019-12-02 01:40:47',0,0,0.00),(117,8,'2019-12-02 01:45:20',1,1,47.40),(118,8,'2019-12-02 01:46:28',1,0,511.65),(119,8,'2019-12-02 01:48:32',1,0,42.00),(120,8,'2019-12-02 01:49:00',1,0,95.80);
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido_item`
--

DROP TABLE IF EXISTS `pedido_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedido_item` (
  `pedido_id` int(11) NOT NULL,
  `produto_id` int(11) NOT NULL,
  `quantidade` int(11) NOT NULL,
  `preco` decimal(6,2) NOT NULL,
  `observacao` varchar(200) NOT NULL,
  PRIMARY KEY (`pedido_id`,`produto_id`),
  KEY `pedido_id_idx` (`pedido_id`),
  KEY `produto_id` (`produto_id`),
  CONSTRAINT `pedido_id` FOREIGN KEY (`pedido_id`) REFERENCES `pedido` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `produto_id` FOREIGN KEY (`produto_id`) REFERENCES `produto` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido_item`
--

LOCK TABLES `pedido_item` WRITE;
/*!40000 ALTER TABLE `pedido_item` DISABLE KEYS */;
INSERT INTO `pedido_item` VALUES (86,20,6,160.20,'sem tomate'),(86,36,3,54.90,'sem maionese'),(87,24,1,33.50,'sem salada'),(87,33,7,95.55,'com chocolate duplo'),(87,65,3,23.85,'sem sal'),(88,36,1,18.30,'N\\u00e3o'),(88,39,32,596.80,'Sim'),(88,51,5,260.00,'N\\u00e3o'),(88,65,3,23.85,'N\\u00e3o'),(89,51,2,104.00,'com chocolate preto'),(90,62,3,71.85,'sem alface'),(91,22,5,131.50,'N\\u00e3o'),(93,19,3,80.10,'sem alho'),(93,28,3,45.90,'sem maionese'),(94,20,2,53.40,'Sem milho'),(94,62,3,71.85,'N\\u00e3o'),(95,46,1,6.00,'Sem p\\u00e3o'),(96,55,30,1378.50,'Sem frango'),(96,56,31,1333.00,'Sem carne'),(97,48,4,22.00,'n\\u00e3o'),(98,28,32,489.60,'Sem galinha'),(98,40,4,76.00,'Sem massa'),(98,61,2,37.40,'Sem brigadeiro'),(99,61,4,74.80,'com recheio de brigadeiro branco'),(100,45,2,7.90,'n\\u00e3o'),(101,15,2,60.00,'n\\u00e3o'),(102,12,1,26.30,'n\\u00e3o'),(103,20,2,53.40,'n\\u00e3o'),(104,14,2,67.00,'n\\u00e3o'),(105,12,1,26.30,'n\\u00e3o'),(106,19,1,26.70,'2'),(107,20,1,26.70,'n\\u00e3o'),(108,15,2,60.00,'Sem Observação'),(109,40,5,95.00,'Sem Observação'),(109,48,5,27.50,'Sem Observação'),(110,48,12,66.00,'Sem Observação'),(111,13,5,135.50,'Sem Observação'),(112,14,2,67.00,'Sem Observação'),(112,53,3,148.50,'sem lactose'),(113,55,4,183.80,'Sem Observação'),(113,61,1,18.70,'Sem brigadeiro'),(114,27,6,78.00,'Sem Observação'),(114,40,6,114.00,'Nao'),(115,11,150,3450.00,'Sem Observação'),(117,45,12,47.40,'Sem p\\u00e3o'),(118,39,21,391.65,'Sem Observação'),(118,66,30,120.00,'Coxinha em formato de chinelo'),(119,34,3,42.00,'Chocolate rosa'),(120,62,4,95.80,'Sem Observação');
/*!40000 ALTER TABLE `pedido_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produto`
--

DROP TABLE IF EXISTS `produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `produto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoria_id` int(11) NOT NULL,
  `descricao` varchar(100) NOT NULL,
  `preco` decimal(6,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `categoria_id_idx` (`categoria_id`),
  CONSTRAINT `categoria_id` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produto`
--

LOCK TABLES `produto` WRITE;
/*!40000 ALTER TABLE `produto` DISABLE KEYS */;
INSERT INTO `produto` VALUES (11,26,'Pizza de Calabresa',23.00),(12,26,'Pizza de Frango',26.30),(13,26,'Pizza de Alho',27.10),(14,26,'Pizza de Picanha',33.50),(15,26,'Pizza de Sorvete',30.00),(16,26,'Pizza de Chocolate',24.00),(17,26,'Pizza de Morango com Chocolate',28.90),(19,26,'Pizza de 4 Queijos',26.70),(20,26,'Pizza de Milho',26.70),(21,27,'Hamburger de Carne',23.00),(22,27,'Hamburger de Frango',26.30),(23,27,'Hamburger de File',27.10),(24,27,'Hamburger de Picanha',33.50),(25,27,'Hamburger de Porco',30.00),(26,27,'Hamburger de Mussarela',24.00),(27,29,'Pastel de Carne',13.00),(28,29,'Pastel de Frango',15.30),(29,29,'Pastel de File',17.10),(30,29,'Pastel de Picanha',13.50),(31,29,'Pastel de Porco',14.65),(32,29,'Pastel de 4 Queijos',15.00),(33,29,'Pastel de Chocolate',13.65),(34,29,'Pastel de Chocolate Branco',14.00),(35,30,'Massa com Carne',17.00),(36,30,'Massa com Frango',18.30),(37,30,'Massa com File',21.10),(38,30,'Massa com Picanha',24.50),(39,30,'Massa com Porco',18.65),(40,30,'Massa com 4 Queijos',19.00),(41,32,'Sanduíche de Frango',4.10),(42,32,'Sanduíche Integral',4.50),(43,32,'Sanduíche de Mussarela',4.00),(44,32,'Sanduíche de Carne',5.00),(45,41,'Torrada Simples',3.95),(46,41,'Torrada Completa',6.00),(48,41,'Torrada de Carne',5.50),(49,41,'Torrada de Mortadela',4.00),(50,37,'Torta de Chocolate',46.95),(51,37,'Torta de Ouro Branco',52.00),(52,37,'Torta de Morango',48.50),(53,37,'Torta de Nutela',49.50),(54,37,'Torta de Brigadeiro',47.00),(55,36,'Torta de Frango',45.95),(56,36,'Torta de Carne',43.00),(57,40,'Rocambole de Chocolate',16.95),(58,40,'Rocambole de Ouro Branco',18.00),(59,40,'Rocambole de Morango',17.50),(60,40,'Rocambole de Nutela',19.50),(61,40,'Rocambole de Brigadeiro',18.70),(62,28,'Alaminuta Grande',23.95),(63,28,'Alaminuta Media',18.00),(64,28,'Alaminuta Pequena',14.50),(65,38,'Coxinha Grande',7.95),(66,38,'Coxinha Media',4.00);
/*!40000 ALTER TABLE `produto` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-12-02  2:14:27
