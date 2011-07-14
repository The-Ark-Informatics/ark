use study;

ALTER TABLE `study`.`correspondences` CHANGE COLUMN `STUDY_MANAGER` `ARK_USER_ID` INT(11) NULL  ;

UPDATE correspondences SET ARK_USER_ID = NULL WHERE ID > 0;

ALTER TABLE `study`.`correspondences`
  ADD CONSTRAINT `correspondences_ark_user_id`
  FOREIGN KEY (`ARK_USER_ID` )
  REFERENCES `study`.`ark_user` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `correspondences_ark_user_id` (`ARK_USER_ID` ASC) ;

