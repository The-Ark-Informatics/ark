ALTER TABLE `reporting`.`search_result` DROP FOREIGN KEY `fk_search_result_search` ;
ALTER TABLE `reporting`.`search_result` 
  ADD CONSTRAINT `fk_search_result_search`
  FOREIGN KEY (`SEARCH_ID` )
  REFERENCES `reporting`.`search` (`ID` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;