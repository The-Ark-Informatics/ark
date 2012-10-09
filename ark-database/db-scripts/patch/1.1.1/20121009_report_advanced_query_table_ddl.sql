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

