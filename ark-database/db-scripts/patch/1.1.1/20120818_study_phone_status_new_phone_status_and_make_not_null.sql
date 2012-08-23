INSERT INTO `study`.`phone_status` (`ID`, `NAME`, `DESCRIPTION`) VALUES (0, 'Unknown', 'Status not known, this will be the default if no status provided');
UPDATE `study`.`phone_status` SET id = 0 WHERE name = 'Unknown';

ALTER TABLE `study`.`phone` DROP FOREIGN KEY `phone_ibfk_3` ;
ALTER TABLE `study`.`phone` CHANGE COLUMN `PHONE_STATUS_ID` `PHONE_STATUS_ID` INT(11) NOT NULL DEFAULT 0  , 
  ADD CONSTRAINT `phone_ibfk_3`
  FOREIGN KEY (`PHONE_STATUS_ID` )
  REFERENCES `study`.`phone_status` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  
-- just a bit over the top and unnecessary.  deleting
ALTER TABLE `study`.`phone` 
DROP INDEX `AREA_CODE` ;
 