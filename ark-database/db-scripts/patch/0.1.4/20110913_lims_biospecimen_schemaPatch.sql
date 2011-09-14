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
