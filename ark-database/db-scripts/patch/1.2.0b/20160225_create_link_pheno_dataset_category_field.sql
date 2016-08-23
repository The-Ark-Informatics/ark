USE `pheno`;
--
-- Table structure for table `link_pheno_dataset_category_field`
--
DROP TABLE IF EXISTS `link_pheno_dataset_category_field`;
CREATE TABLE `link_pheno_dataset_category_field` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STUDY_ID` int(11) NOT NULL,
  `ARK_FUNCTION_ID` int(11) NOT NULL,
  `ARK_USER_ID` int(11) NOT NULL,
  `PHENO_DATASET_CATEGORY_ID` int(11) NOT NULL,
  `PHENO_DATASET_FIELD_ID` int(11) NOT NULL,
  `ORDER_NUMBER` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_CATEGORY_FIELD` (`PHENO_DATASET_CATEGORY_ID`,`PHENO_DATASET_FIELD_ID`,`STUDY_ID`,`ARK_FUNCTION_ID`,`ARK_USER_ID`),
  KEY `fk_link_pheno_dataset_category_field_study_idx` (`STUDY_ID`),
  KEY `fk_link_pheno_dataset_category_field_ark_function_idx` (`ARK_FUNCTION_ID`),
  KEY `fk_link_pheno_dataset_category_field_ark_user_idx` (`ARK_USER_ID`),
  KEY `fk_link_pheno_dataset_category_field_pheno_field_idx` (`PHENO_DATASET_FIELD_ID`),
  CONSTRAINT `fk_link_pheno_dataset_category_field_ark_function` FOREIGN KEY (`ARK_FUNCTION_ID`) REFERENCES `study`.`ark_function` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_link_pheno_dataset_category_field_ark_user` FOREIGN KEY (`ARK_USER_ID`) REFERENCES `study`.`ark_user` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_link_pheno_dataset_category_field_pheno_cat` FOREIGN KEY (`PHENO_DATASET_CATEGORY_ID`) REFERENCES `pheno_dataset_category` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_link_pheno_dataset_category_field_pheno_field` FOREIGN KEY (`PHENO_DATASET_FIELD_ID`) REFERENCES `pheno_dataset_field` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_link_pheno_dataset_category_field_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=latin1;

