USE reporting;

-- Add FK to [ark_module] table
ALTER TABLE `reporting`.`report_template` 
  ADD CONSTRAINT `FK_REPORTTEMPLATE_ARKMODULE`
  FOREIGN KEY (`MODULE_ID` )
  REFERENCES `study`.`ark_module` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `FK_REPORTTEMPLATE_ARKMODULE` (`MODULE_ID` ASC) ;
