use study;
/* Reason:We don't need to enforce sutdy status from the backend. We can do that from the application where and when needed. One of the places we don't have a study status is when we update a user's my details. A study status cannot be associated in that context.*/
 
ALTER TABLE `study`.`audit_history` DROP FOREIGN KEY `audit_history_ibfk_1` ;
ALTER TABLE `study`.`audit_history` CHANGE COLUMN `STUDY_STATUS_ID` `STUDY_STATUS_ID` INT(11) NULL DEFAULT '0'  , 
  ADD CONSTRAINT `audit_history_ibfk_1`
  FOREIGN KEY (`STUDY_STATUS_ID` )
  REFERENCES `study`.`study_status` (`ID` )
, DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

