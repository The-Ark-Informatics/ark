USE lims;

-- We are about to change the 'treatment' column to a FK to TreatmentType table
UPDATE `lims`.`biospecimen` SET `TREATMENT`='1' WHERE id > 0;

-- Now we can make the 'treament' column NOT NULL and a FK to TreatmentType table
ALTER TABLE `lims`.`biospecimen` CHANGE COLUMN `TREATMENT` `TREATMENT_TYPE_ID` INT NOT NULL  , 
  ADD CONSTRAINT `fk_biospecimen_treatment_type_id`
  FOREIGN KEY (`TREATMENT_TYPE_ID` )
  REFERENCES `lims`.`treatment_type` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_biospecimen_treatment_type_id` (`TREATMENT_TYPE_ID` ASC) ;

-- quantity is sum of all transactions
ALTER TABLE `lims`.`biospecimen` CHANGE COLUMN `QUANTITY` `QUANTITY` DOUBLE NULL DEFAULT NULL  ;

-- Change units to unit_id (reference table)
ALTER TABLE `lims`.`biospecimen` CHANGE COLUMN `UNITS` `UNIT_ID` INT(11) NULL DEFAULT NULL  , 
  ADD CONSTRAINT `fk_biospecimen_unit`
  FOREIGN KEY (`UNIT_ID` )
  REFERENCES `lims`.`unit` (`ID` )
  ON UPDATE CASCADE
, ADD INDEX `fk_biospecimen_unit` (`UNIT_ID` ASC) ;
