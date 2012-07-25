USE admin;

delimiter $$

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

delimiter $$

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

delimiter $$

CREATE TABLE `link_billable_item_subject` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BILLABLE_ITEM_ID` int(11) DEFAULT NULL,
  `SUBJECT_ID` int(11) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_link_billable_item_subject_bi_id` (`BILLABLE_ITEM_ID`),
  CONSTRAINT `fk_link_billable_item_subject_bi_id` FOREIGN KEY (`BILLABLE_ITEM_ID`) REFERENCES `bilable_item` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

delimiter $$

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

delimiter $$

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$
