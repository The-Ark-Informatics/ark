CREATE DATABASE  IF NOT EXISTS `admin`;

USE `admin`;

DROP TABLE IF EXISTS `billable_item_status`;
CREATE TABLE `billable_item_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `billable_item_status`
--

LOCK TABLES `billable_item_status` WRITE;
INSERT INTO `billable_item_status` VALUES (1,'Not Commenced',''),(2,'Commenced',''),(3,'Completed','');
UNLOCK TABLES;

--
-- Table structure for table `researcher_role`
--
DROP TABLE IF EXISTS `researcher_role`;
CREATE TABLE `researcher_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `researcher_role`
--

LOCK TABLES `researcher_role` WRITE;
INSERT INTO `researcher_role` VALUES (1,'Chief Investigator',NULL),(2,'Assoc Investigator',NULL),(3,'Other Investigator',NULL),(4,'Research Assistant',NULL);
UNLOCK TABLES;

--
-- Table structure for table `billable_item_type_status`
--

DROP TABLE IF EXISTS `billable_item_type_status`;
CREATE TABLE `billable_item_type_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `billable_item_type_status`
--

LOCK TABLES `billable_item_type_status` WRITE;
INSERT INTO `billable_item_type_status` VALUES (1,'ACTIVE','Active record'),(2,'INACTIVE','Inactive record');
UNLOCK TABLES;

--
-- Table structure for table `work_request_status`
--

DROP TABLE IF EXISTS `work_request_status`;
CREATE TABLE `work_request_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `work_request_status`
--

LOCK TABLES `work_request_status` WRITE;
INSERT INTO `work_request_status` VALUES (1,'Not Commenced',''),(2,'Commenced',''),(3,'Completed','');
UNLOCK TABLES;

--
-- Table structure for table `billing_type`
--

DROP TABLE IF EXISTS `billing_type`;
CREATE TABLE `billing_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `billing_type`
--

LOCK TABLES `billing_type` WRITE;
INSERT INTO `billing_type` VALUES (1,'CASH',NULL),(2,'CHEQUE',NULL),(3,'DRAFT',NULL);
UNLOCK TABLES;

--
-- Table structure for table `researcher_status`
--
DROP TABLE IF EXISTS `researcher_status`;
CREATE TABLE `researcher_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(25) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `researcher_status`
--

LOCK TABLES `researcher_status` WRITE;
INSERT INTO `researcher_status` VALUES (1,'Active',NULL),(2,'Inactive','');
UNLOCK TABLES;


CREATE TABLE `researcher` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(30) DEFAULT NULL,
  `LAST_NAME` varchar(45) DEFAULT NULL,
  `INSTITUTE` varchar(50) DEFAULT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  `STATUS_ID` int(11) DEFAULT NULL,
  `CREATED_DATE` date DEFAULT NULL,
  `OFFICE_PHONE` varchar(12) DEFAULT NULL,
  `MOBILE` varchar(12) DEFAULT NULL,
  `EMAIL` varchar(45) DEFAULT NULL,
  `FAX` varchar(12) DEFAULT NULL,
  `COMMENT` varchar(255) DEFAULT NULL,
  `BILLING_TYPE_ID` int(11) DEFAULT NULL,
  `ACCOUNT_NUMBER` varchar(30) DEFAULT NULL,
  `BSB` varchar(8) DEFAULT NULL,
  `BANK` varchar(50) DEFAULT NULL,
  `ACCOUNT_NAME` varchar(50) DEFAULT NULL,
  `STUDY_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_researcher_role_id` (`ROLE_ID`),
  KEY `fk_researcher_status_id` (`STATUS_ID`),
  KEY `fk_researcher_billing_type_id` (`BILLING_TYPE_ID`),
  CONSTRAINT `fk_researcher_billing_type_id` FOREIGN KEY (`BILLING_TYPE_ID`) REFERENCES `billing_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_researcher_role_id` FOREIGN KEY (`ROLE_ID`) REFERENCES `researcher_role` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_researcher_status_id` FOREIGN KEY (`STATUS_ID`) REFERENCES `researcher_status` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `billable_item_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ITEM_NAME` varchar(45) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `QUANTITY_PER_UNIT` int(11) DEFAULT NULL,
  `UNIT_PRICE` double DEFAULT NULL,
  `GST` double DEFAULT NULL,
  `STATUS_ID` int(1) DEFAULT '1',
  `STUDY_ID` int(11) NOT NULL,
  `TYPE` varchar(10) NOT NULL DEFAULT 'DEFAULT',
  PRIMARY KEY (`ID`),
  KEY `fk_billable_item_type_study_id` (`STUDY_ID`),
  KEY `fk_billable_item_type_status_id` (`STATUS_ID`),
  CONSTRAINT `fk_billable_item_type_status_id` FOREIGN KEY (`STATUS_ID`) REFERENCES `billable_item_type_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_billable_item_type_study_id` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `work_request` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `STATUS_ID` int(11) DEFAULT NULL,
  `RESEARCHER_ID` int(11) DEFAULT NULL,
  `REQUESTED_DATE` date DEFAULT NULL,
  `COMMENCED_DATE` date DEFAULT NULL,
  `COMPLETED_DATE` date DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_work_request_researcher_id` (`RESEARCHER_ID`),
  KEY `fk_work_request_status_id` (`STATUS_ID`),
  CONSTRAINT `fk_work_request_researcher_id` FOREIGN KEY (`RESEARCHER_ID`) REFERENCES `researcher` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_work_request_status_id` FOREIGN KEY (`STATUS_ID`) REFERENCES `work_request_status` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `bilable_item` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `QUANTITY` int(11) DEFAULT NULL,
  `COMMENCE_DATE` date DEFAULT NULL,
  `TYPE` varchar(10) NOT NULL,
  `STATUS_ID` int(11) DEFAULT NULL,
  `REQUEST_ID` int(11) DEFAULT NULL,
  `INVOICE` varchar(5) DEFAULT NULL,
  `STUDY_ID` int(11) NOT NULL,
  `BILLABLE_TYPE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_bilable_item_billable_type` (`BILLABLE_TYPE`),
  KEY `fk_bilable_item_request_id` (`REQUEST_ID`),
  KEY `fk_bilable_item_status` (`STATUS_ID`),
  CONSTRAINT `fk_bilable_item_billable_type` FOREIGN KEY (`BILLABLE_TYPE`) REFERENCES `billable_item_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bilable_item_request_id` FOREIGN KEY (`REQUEST_ID`) REFERENCES `work_request` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_bilable_item_status` FOREIGN KEY (`STATUS_ID`) REFERENCES `billable_item_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `link_billable_item_subject` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BILLABLE_ITEM_ID` int(11) DEFAULT NULL,
  `SUBJECT_ID` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_link_billable_item_subject_bi_id` (`BILLABLE_ITEM_ID`),
  CONSTRAINT `fk_link_billable_item_subject_bi_id` FOREIGN KEY (`BILLABLE_ITEM_ID`) REFERENCES `bilable_item` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Rename the researcher table column institute to organization

ALTER TABLE `admin`.`researcher` CHANGE COLUMN `INSTITUTE` `ORGANIZATION` VARCHAR(50) NULL DEFAULT NULL  ;

-- Additional changes for the billable_item and researcher tables

ALTER TABLE `admin`.`bilable_item` ADD COLUMN `TOTAL_COST` DOUBLE NULL AFTER `BILLABLE_TYPE`;

ALTER TABLE `admin`.`bilable_item` ADD COLUMN `ITEM_COST` DOUBLE NULL  AFTER `TOTAL_COST` ;

ALTER TABLE `admin`.`bilable_item` ADD COLUMN `ATTACHMENT_FILENAME` VARCHAR(45) NULL  AFTER `TOTAL_COST` , ADD COLUMN `ATTACHMENT_PAYLOAD` LONGBLOB NULL  AFTER `ATTACHMENT_FILENAME` ;

ALTER TABLE `admin`.`researcher` ADD COLUMN `TITLE_TYPE_ID` INT(11) NULL  AFTER `STUDY_ID` ;

ALTER TABLE `admin`.`bilable_item` ADD COLUMN `TOTAL_GST` DOUBLE NULL  AFTER `ATTACHMENT_PAYLOAD` ;

-- Add the Researcher Recruitment Costs - Summary of Account report
INSERT INTO `reporting`.`report_template`
(`NAME`,
`DESCRIPTION`,
`TEMPLATE_PATH`,
`MODULE_ID`,
`FUNCTION_ID`)
VALUES
(
'Work Researcher Cost Report',
'This report lists the total billable item type costs realated to a researcher',
'ResearcherCostReport.jrxml',
(select ID from study.ark_module where name='Work Tracking'),
(select ID from study.ark_function where name='BILLABLE_ITEM')
);

-- Rename the Work module to Work Tracking
UPDATE `study`.`ark_module` SET `NAME`='Work Tracking' WHERE `NAME`='Work'

-- Researcher detail cost report
INSERT INTO `reporting`.`report_template` (`NAME`, `DESCRIPTION`, `TEMPLATE_PATH`, `MODULE_ID`, `FUNCTION_ID`) 
VALUES 
('Work Researcher Detail Cost Report', 'This report lists the individual billable item costs group by the billable item type realated to a researcher', 'ResearcherCostReport.jrxml', 
(select ID from study.ark_module where name='Work Tracking'),
(select ID from study.ark_function where name='BILLABLE_ITEM')
);


-- Study detail cost report
INSERT INTO `reporting`.`report_template` (`NAME`, `DESCRIPTION`, `TEMPLATE_PATH`, `MODULE_ID`, `FUNCTION_ID`) 
VALUES 
('Work Study Detail Cost Report', 'This report lists the individual billable item costs group by the billable item type related to context study', 'StudyDetailCostReport.jrxml', 
(select ID from study.ark_module where name='Work Tracking'),
(select ID from study.ark_function where name='BILLABLE_ITEM')
);

-- Researcher detail cost report related changes. 
ALTER TABLE `admin`.`bilable_item` ADD COLUMN `GST` DOUBLE NULL  AFTER `TOTAL_GST` ;

ALTER TABLE `admin`.`bilable_item` ADD COLUMN `GST_ALLOW` VARCHAR(45) NULL  AFTER `GST` ;

ALTER TABLE `admin`.`bilable_item` CHANGE COLUMN `QUANTITY` `QUANTITY` DOUBLE NULL DEFAULT NULL  ;

ALTER TABLE `admin`.`billable_item_type` ADD COLUMN `QUANTITY_TYPE` VARCHAR(45) NULL  AFTER `TYPE` ;

ALTER TABLE `admin`.`billable_item_type` DROP COLUMN `GST` ;

ALTER TABLE `admin`.`bilable_item` DROP FOREIGN KEY `fk_bilable_item_status` ;

drop table `admin`.`billable_item_status`;

drop table `admin`.`link_billable_item_subject`;






