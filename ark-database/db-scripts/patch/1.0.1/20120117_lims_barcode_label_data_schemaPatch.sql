USE lims;
ALTER TABLE `lims`.`barcode_label` CHANGE COLUMN `STUDY_ID` `STUDY_ID` INT(11) NULL  ;


ALTER TABLE `lims`.`barcode_label_data` 
  ADD CONSTRAINT `fk_barcode_label_data_1`
  FOREIGN KEY (`BARCODE_LABEL_ID` )
  REFERENCES `lims`.`barcode_label` (`ID` )
  ON DELETE CASCADE
  ON UPDATE CASCADE;
    
ALTER TABLE `lims`.`barcode_label` ADD COLUMN `VERSION` INT(11) NOT NULL DEFAULT 1  AFTER `LABEL_SUFFIX` ;

