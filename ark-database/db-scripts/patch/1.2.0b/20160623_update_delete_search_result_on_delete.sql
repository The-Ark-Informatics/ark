USE `reporting`;

ALTER TABLE `search_result` DROP FOREIGN KEY `fk_search_result_search`;
ALTER TABLE `search_result` DROP FOREIGN KEY `search_result_ibfk_2`;
ALTER TABLE `search_result` DROP INDEX `fk_search_result_search`;

ALTER TABLE `search_result` ADD CONSTRAINT `search_result_ibfk_2` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `search_result` ADD CONSTRAINT `fk_search_result_search` FOREIGN KEY (`SEARCH_ID`) REFERENCES `search` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE;

