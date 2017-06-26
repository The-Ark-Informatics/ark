-- Make study_id column not null
ALTER TABLE `study`.`ark_user_role` 
DROP FOREIGN KEY `FK_ARK_USER_ROLE_STUDY_ID`;
ALTER TABLE `study`.`ark_user_role` 
CHANGE COLUMN `STUDY_ID` `STUDY_ID` INT(11) NOT NULL ;
ALTER TABLE `study`.`ark_user_role` 
ADD CONSTRAINT `FK_ARK_USER_ROLE_STUDY_ID`
  FOREIGN KEY (`STUDY_ID`)
  REFERENCES `study`.`study` (`ID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

-- Rename foriegn key to standard name
ALTER TABLE `study`.`ark_user_role` 
DROP INDEX `FK_ARK_USER_ROLE_STUDY_ID` ,
ADD INDEX `FK_ARK_STUDY_ID` (`STUDY_ID` ASC);

-- Delete duplicate entries

DELETE userRoleX 
FROM `study`.`ark_user_role` userRoleX
JOIN `study`.`ark_user_role` userRoleY
ON userRoleY.ark_user_id= userRoleX.ark_user_id
AND userRoleY.ark_role_id = userRoleX.ark_role_id
AND userRoleY.ark_module_id = userRoleX.ark_module_id
AND userRoleY.study_id = userRoleX.study_id
AND userRoleY.id < userRoleX.id;

-- Add a unique key to the table

ALTER TABLE `study`.`ark_user_role` 
ADD UNIQUE INDEX `UK_ARK_USER_ROLE` (`STUDY_ID` ASC, `ARK_MODULE_ID` ASC, `ARK_ROLE_ID` ASC, `ARK_USER_ID` ASC);



