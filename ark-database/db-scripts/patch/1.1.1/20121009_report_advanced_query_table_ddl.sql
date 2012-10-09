CREATE  TABLE `reporting`.`query_grouping` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(45) NULL ,
  PRIMARY KEY (`ID`) );










CREATE  TABLE `reporting`.`search` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(255) NULL ,
  `TOP_LEVEL_GROUPING_ID` INT NULL ,
  PRIMARY KEY (`ID`) );

ALTER TABLE `reporting`.`search` 
ADD INDEX `fk_search_query_grouping` (`TOP_LEVEL_GROUPING_ID` ASC) ;




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
COMMENT = 'many to many join custom_field_display and search';

