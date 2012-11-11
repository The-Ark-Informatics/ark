DROP  TABLE IF EXISTS `reporting`.`biocollection_field`;
CREATE  TABLE `reporting`.`biocollection_field` (
  `ID` INT NOT NULL AUTO_INCREMENT  ,
  `ENTITY` VARCHAR(255) NULL ,
  `FIELD_NAME` VARCHAR(255) NULL ,
  `PUBLIC_FIELD_NAME` VARCHAR(255) NULL ,
  `FIELD_TYPE_ID` INT NULL ,
  PRIMARY KEY (`ID`) ,
  INDEX `fk_biocollection_field_field_type` (`FIELD_TYPE_ID` ASC) ,
  CONSTRAINT `fk_biocollection_field_field_type`
    FOREIGN KEY (`FIELD_TYPE_ID` )
    REFERENCES `study`.`field_type` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB ;


DROP  TABLE IF EXISTS `reporting`.`biocollection_field_search`;
CREATE TABLE `reporting`.`biocollection_field_search` (   
	`ID` int(11)  NOT NULL AUTO_INCREMENT ,   
	`BIOCOLLECTION_FIELD_ID` int(11) DEFAULT NULL,   
	`SEARCH_ID` int(11) DEFAULT NULL,   
	PRIMARY KEY (`ID`),   
	KEY `fk_dfs_biocollection_field` (`BIOCOLLECTION_FIELD_ID`),
   	KEY `fk_dfs_search` (`SEARCH_ID`) ) 
ENGINE = InnoDB 
COMMENT='many2many join biocollection_field and search';

ALTER TABLE `reporting`.`biocollection_field_search` 
ADD UNIQUE INDEX `uq_dfs_df_s` (`BIOCOLLECTION_FIELD_ID` ASC, `SEARCH_ID` ASC) ;




DROP  TABLE IF EXISTS `reporting`.`biospecimen_field` ;
CREATE  TABLE `reporting`.`biospecimen_field` (
  `ID` INT NOT NULL AUTO_INCREMENT  ,
  `ENTITY` VARCHAR(255) NULL ,
  `FIELD_NAME` VARCHAR(255) NULL ,
  `PUBLIC_FIELD_NAME` VARCHAR(255) NULL ,
  `FIELD_TYPE_ID` INT NULL ,
  PRIMARY KEY (`ID`) ,
  INDEX `fk_biospecimen_field_field_type` (`FIELD_TYPE_ID` ASC) ,
  CONSTRAINT `fk_biospecimen_field_field_type`
    FOREIGN KEY (`FIELD_TYPE_ID` )
    REFERENCES `study`.`field_type` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB ;


DROP  TABLE IF EXISTS `reporting`.`biospecimen_field_search`;
CREATE TABLE `reporting`.`biospecimen_field_search` (   
	`ID` int(11)  NOT NULL AUTO_INCREMENT ,   
	`BIOSPECIMEN_FIELD_ID` int(11) DEFAULT NULL,   
	`SEARCH_ID` int(11) DEFAULT NULL,   
	PRIMARY KEY (`ID`),   
	KEY `fk_dfs_biospecimen_field` (`BIOSPECIMEN_FIELD_ID`),
   	KEY `fk_dfs_search` (`SEARCH_ID`) ) 
ENGINE = InnoDB 
COMMENT='many2many join biospecimen_field and search';

ALTER TABLE `reporting`.`biospecimen_field_search` 
ADD UNIQUE INDEX `uq_dfs_df_s` (`BIOSPECIMEN_FIELD_ID` ASC, `SEARCH_ID` ASC) ;








INSERT INTO `reporting`.`demographic_field` (`ENTITY`, `FIELD_NAME`, `PUBLIC_FIELD_NAME`, `FIELD_TYPE_ID`) 
VALUES ('LinkSubjectStudy', 'consentDate', 'Subject Constent Date', '3');
INSERT INTO `reporting`.`demographic_field` (`ENTITY`, `FIELD_NAME`, `PUBLIC_FIELD_NAME`, `FIELD_TYPE_ID`) 
VALUES ('LinkSubjectStudy', 'subjectUID', 'Subject UID', '1');
INSERT INTO `reporting`.`demographic_field` (`ENTITY`, `FIELD_NAME`, `PUBLIC_FIELD_NAME`, `FIELD_TYPE_ID`) 
VALUES ('Person', 'firstName', 'First Name', '1');
INSERT INTO `reporting`.`demographic_field` (`ENTITY`, `FIELD_NAME`, `PUBLIC_FIELD_NAME`, `FIELD_TYPE_ID`) 
VALUES ('Address', 'postCode', 'Post Code', '1');



ALTER TABLE `reporting`.`query_filter` ADD COLUMN `BIOSPECIMEN_FIELD_ID` INT NULL  AFTER `PREFIX` , ADD COLUMN `BIOCOLLECTION_FIELD_ID` INT NULL  AFTER `BIOSPECIMEN_FIELD_ID` , 
  ADD CONSTRAINT `fk_qf_bsf`
  FOREIGN KEY (`BIOSPECIMEN_FIELD_ID` )
  REFERENCES `reporting`.`biospecimen_field` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION, 
  ADD CONSTRAINT `fk_qf_bcf`
  FOREIGN KEY (`BIOCOLLECTION_FIELD_ID` )
  REFERENCES `reporting`.`biocollection_field` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_qf_bsf_idx` (`BIOSPECIMEN_FIELD_ID` ASC) 
, ADD INDEX `fk_qf_bcf_idx` (`BIOCOLLECTION_FIELD_ID` ASC) ;


