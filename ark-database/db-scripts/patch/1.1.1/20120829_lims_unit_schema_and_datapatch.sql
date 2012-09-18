INSERT INTO lims.unit (ID, NAME) VALUES (0, 'unit');
UPDATE lims.unit SET ID = 0 WHERE NAME= 'unit';
UPDATE lims.biospecimen SET UNIT_ID = 0 where unit_id is null;

ALTER TABLE `lims`.`biospecimen` DROP FOREIGN KEY `fk_biospecimen_unit` ;
ALTER TABLE `lims`.`biospecimen` CHANGE COLUMN `UNIT_ID` `UNIT_ID` INT(11) NULL DEFAULT 0  , 
  ADD CONSTRAINT `fk_biospecimen_unit`
  FOREIGN KEY (`UNIT_ID` )
  REFERENCES `lims`.`unit` (`ID` )
  ON UPDATE CASCADE;
