-- Add the constraint ot CASCADE DELETE on delete of function

ALTER TABLE `study`.`ark_module_function` DROP FOREIGN KEY `FK_ARK_MODULE_FUNCTION_ARK_FUNCTION_ID` ;
ALTER TABLE `study`.`ark_module_function` 
  ADD CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_FUNCTION_ID`
  FOREIGN KEY (`ARK_FUNCTION_ID` )
  REFERENCES `study`.`ark_function` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;