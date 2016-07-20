USE `reporting`;

CREATE TABLE `reporting`.`pheno_dataset_field_display_search` (
	  `ID` int(11) NOT NULL AUTO_INCREMENT,
	  `PHENO_DATASET_FIELD_DISPLAY_ID` int(11) DEFAULT NULL,
	  `SEARCH_ID` int(11) DEFAULT NULL,
	  PRIMARY KEY (`ID`),
	  UNIQUE KEY `uq_pdfds_pdfd_search` (`PHENO_DATASET_FIELD_DISPLAY_ID`,`SEARCH_ID`),
	  KEY `fk_pdfds_pheno_dataset_field_display` (`PHENO_DATASET_FIELD_DISPLAY_ID`),
	  KEY `fk_pdfds_search` (`SEARCH_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=491 DEFAULT CHARSET=latin1 COMMENT='many2many join pheno_dataset_field_display and search';
