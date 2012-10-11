DROP TABLE `reporting`.`query_grouping` ;

CREATE  TABLE `reporting`.`query_grouping` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(45) NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB ;



DROP  TABLE `reporting`.`search` ;
CREATE  TABLE `reporting`.`search` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(255) NULL ,
  `TOP_LEVEL_GROUPING_ID` INT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB ;

ALTER TABLE `reporting`.`search` 
ADD INDEX `fk_search_query_grouping` (`TOP_LEVEL_GROUPING_ID` ASC) ;


DROP  TABLE `reporting`.`custom_field_display_search`;
CREATE  TABLE `reporting`.`custom_field_display_search` (
  `ID` INT NOT NULL ,
  `CUSTOM_FIELD_DISPLAY_ID` INT NULL ,
  `SEARCH_ID` INT NULL ,
  PRIMARY KEY (`ID`) ,
  INDEX `fk_cfds_custom_field_display` (`CUSTOM_FIELD_DISPLAY_ID` ASC) ,
  INDEX `fk_cfds_search` (`SEARCH_ID` ASC) ,
  CONSTRAINT `fk_cfds_custom_field_display`
    FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID` )
    REFERENCES `study`.`custom_field_display` (`ID` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_cfds_search`
    FOREIGN KEY (`SEARCH_ID` )
    REFERENCES `reporting`.`search` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'many2many join custom_field_display and search'
ENGINE = InnoDB ;


DROP  TABLE `reporting`.`demographic_field` ;
CREATE  TABLE `reporting`.`demographic_field` (
  `ID` INT NOT NULL ,
  `ENTITY` VARCHAR(255) NULL ,
  `FIELD_NAME` VARCHAR(255) NULL ,
  `PUBLIC_FIELD_NAME` VARCHAR(255) NULL ,
  `FIELD_TYPE_ID` INT NULL ,
  PRIMARY KEY (`ID`) ,
  INDEX `fk_demographic_field_field_type` (`FIELD_TYPE_ID` ASC) ,
  CONSTRAINT `fk_demographic_field_field_type`
    FOREIGN KEY (`FIELD_TYPE_ID` )
    REFERENCES `study`.`field_type` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB ;


DROP  TABLE `reporting`.`demographic_field_search`;
CREATE TABLE `reporting`.`demographic_field_search` (   
	`ID` int(11) NOT NULL,   
	`CUSTOM_FIELD_DISPLAY_ID` int(11) DEFAULT NULL,   
	`SEARCH_ID` int(11) DEFAULT NULL,   
	PRIMARY KEY (`ID`),   
	KEY `fk_dfs_custom_field_display` (`CUSTOM_FIELD_DISPLAY_ID`),
   	KEY `fk_dfs_search` (`SEARCH_ID`) ) 
ENGINE = InnoDB 
COMMENT='many2many join demographic_field and search';



DROP  TABLE `reporting`.`query_filter` ;
CREATE  TABLE `reporting`.`query_filter` (
  `ID` INT NOT NULL ,
  `DEMOGRAPHIC_FIELD_ID` INT NULL ,
  `CUSTOM_FIELD_DISPLAY_ID` INT NULL ,
  `VALUE` VARCHAR(512) NULL ,
  `SECOND_VALUE` VARCHAR(512) NULL ,
  `OPERATOR` VARCHAR(256) NULL ,
  `PREFIX` VARCHAR(56) NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB ;

ALTER TABLE `reporting`.`query_filter` 
  ADD CONSTRAINT `fk_qf_df`
  FOREIGN KEY (`DEMOGRAPHIC_FIELD_ID` )
  REFERENCES `reporting`.`demographic_field` (`ID` )
  ON DELETE CASCADE
  ON UPDATE CASCADE
, ADD INDEX `fk_qf_df_idx` (`DEMOGRAPHIC_FIELD_ID` ASC) ;

ALTER TABLE `reporting`.`query_filter` 
  ADD CONSTRAINT `fk_qf_cfd`
  FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID` )
  REFERENCES `study`.`custom_field_display` (`ID` )
  ON DELETE CASCADE
  ON UPDATE CASCADE
, ADD INDEX `fk_qf_cfd_idx` (`CUSTOM_FIELD_DISPLAY_ID` ASC) ;







DROP  TABLE `reporting`.`query_filter_grouping`;
CREATE  TABLE `reporting`.`query_filter_grouping` (
  `ID` INT NOT NULL ,
  `PARENT_GROUPING` INT NOT NULL ,
  `LEFT_FILTER` INT NOT NULL ,
  `JOIN_TO_NEXT_FILTER` VARCHAR(56) NULL ,
  `FILTER_PRECEDENCE` INT NULL ,
  PRIMARY KEY (`ID`) ,
  INDEX `fk_qfg_qg_idx` (`PARENT_GROUPING` ASC) ,
  INDEX `fk_qfg_qf_idx` (`LEFT_FILTER` ASC) ,
  CONSTRAINT `fk_qfg_qg`
    FOREIGN KEY (`PARENT_GROUPING` )
    REFERENCES `reporting`.`query_grouping` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_qfg_qf`
    FOREIGN KEY (`LEFT_FILTER` )
    REFERENCES `reporting`.`query_filter` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
# TODO analyse whether we want cascade.



CREATE  TABLE `reporting`.`query_grouping_grouping` (
  `ID` INT NOT NULL ,
  `PARENT_GROUPING` INT NOT NULL ,
  `LEFT_GROUPING` INT NOT NULL ,
  `JOIN_TO_NEXT_FILTER` VARCHAR(56) NULL ,
  `PRECEDENCE` INT NULL ,
  PRIMARY KEY (`ID`) ,
  INDEX `fk_qgg_parent_grouping_idx` (`PARENT_GROUPING` ASC) ,
  INDEX `fk_qgg_left_grouping_idx` (`LEFT_GROUPING` ASC) ,
  CONSTRAINT `fk_qgg_parent_grouping`
    FOREIGN KEY (`PARENT_GROUPING` )
    REFERENCES `reporting`.`query_grouping` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_qgg_left_grouping`
    FOREIGN KEY (`LEFT_GROUPING` )
    REFERENCES `reporting`.`query_grouping` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
# TODO analyse whether we want cascade.








