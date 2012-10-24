-- Change qty columns to decimal format. DOUBLE was causing floating point precision errors
ALTER TABLE `lims`.`biospecimen` CHANGE COLUMN `QUANTITY` `QUANTITY` DECIMAL NULL DEFAULT NULL  ;
ALTER TABLE `lims`.`bio_transaction` CHANGE COLUMN `QUANTITY` `QUANTITY` DECIMAL NULL DEFAULT NULL  ;