-- Select tables refer to the study.ark_module table

select TABLE_NAME,COLUMN_NAME,CONSTRAINT_NAME,
REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME 
from INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
where REFERENCED_TABLE_NAME = 'ark_module'


--Update foreign key reference to on update cascade in following tables

-- ark_module_function table
 ALTER TABLE `study`.`ark_module_function` DROP FOREIGN KEY `FK_ARK_MODULE_FUNCTION_ARK_MODULE_ID` ;
 ALTER TABLE `study`.`ark_module_function` 
  ADD CONSTRAINT `FK_ARK_MODULE_FUNCTION_ARK_MODULE_ID`
  FOREIGN KEY (`ARK_MODULE_ID` )
  REFERENCES `study`.`ark_module` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE CASCADE;

-- ark_module_role table
ALTER TABLE `study`.`ark_module_role` DROP FOREIGN KEY `fk_ark_module_role_1` ;
ALTER TABLE `study`.`ark_module_role` 
  ADD CONSTRAINT `fk_ark_module_role_1`
  FOREIGN KEY (`ARK_MODULE_ID` )
  REFERENCES `study`.`ark_module` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE CASCADE;


-- ark_role_policy_template table
ALTER TABLE `study`.`ark_role_policy_template` DROP FOREIGN KEY `FK_ROLE_TMPLT_ARK_MODULE_ID` ;
ALTER TABLE `study`.`ark_role_policy_template` 
  ADD CONSTRAINT `FK_ROLE_TMPLT_ARK_MODULE_ID`
  FOREIGN KEY (`ARK_MODULE_ID` )
  REFERENCES `study`.`ark_module` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE CASCADE;

-- ark_user_role table
ALTER TABLE `study`.`ark_user_role` DROP FOREIGN KEY `FK_ARK_MODULE_ID` ;
ALTER TABLE `study`.`ark_user_role` 
  ADD CONSTRAINT `FK_ARK_MODULE_ID`
  FOREIGN KEY (`ARK_MODULE_ID` )
  REFERENCES `study`.`ark_module` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE CASCADE;

-- link_study_arkmodule table
ALTER TABLE `study`.`link_study_arkmodule` DROP FOREIGN KEY `FK_LINK_STUDY_ARKMODULE_ARK_MODULE_ID` ;
ALTER TABLE `study`.`link_study_arkmodule` 
  ADD CONSTRAINT `FK_LINK_STUDY_ARKMODULE_ARK_MODULE_ID`
  FOREIGN KEY (`ARK_MODULE_ID` )
  REFERENCES `study`.`ark_module` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE CASCADE;
-- upload_type table
ALTER TABLE `study`.`upload_type` DROP FOREIGN KEY `fk_upload_type_ark_module` ;
ALTER TABLE `study`.`upload_type` 
  ADD CONSTRAINT `fk_upload_type_ark_module`
  FOREIGN KEY (`ARK_MODULE_ID` )
  REFERENCES `study`.`ark_module` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE CASCADE;

-- report_template table
ALTER TABLE `reporting`.`report_template` DROP FOREIGN KEY `FK_REPORTTEMPLATE_ARKMODULE` ;
ALTER TABLE `reporting`.`report_template` 
  ADD CONSTRAINT `FK_REPORTTEMPLATE_ARKMODULE`
  FOREIGN KEY (`MODULE_ID` )
  REFERENCES `study`.`ark_module` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE CASCADE;


 -- Rename the Work module to Work Tracking
UPDATE `study`.`ark_module` SET `NAME`='Work Tracking' WHERE `NAME`='Work'; 
  
--Initialize the work module id
SET @work_module_id =0; 

--Assign work module id
select @work_module_id:=id from study.ark_module where name='Work Tracking';

--Increment the work module id
-- Update the Admin modue id with incremented work module id
update study.ark_module
set id=(@work_module_id+1)
where name='Admin'


