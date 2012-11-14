ALTER TABLE `reporting`.`query_filter` ADD COLUMN `SEARCH_ID` INT NULL  AFTER `BIOCOLLECTION_FIELD_ID` , 
  ADD CONSTRAINT `fk_qf_search`
  FOREIGN KEY (`SEARCH_ID` )
  REFERENCES `reporting`.`search` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_query_filter_1_idx` (`SEARCH_ID` ASC) ;

