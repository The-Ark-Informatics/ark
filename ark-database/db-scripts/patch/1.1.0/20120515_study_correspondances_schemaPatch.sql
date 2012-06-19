USE study;
ALTER TABLE `study`.`correspondences` DROP FOREIGN KEY `fk_correspondences_ark_user` ;
ALTER TABLE `study`.`correspondences` 
  ADD CONSTRAINT `fk_correspondences_ark_user`
  FOREIGN KEY (`ARK_USER_ID` )
  REFERENCES `study`.`ark_user` (`ID` )
  ON DELETE SET NULL
  ON UPDATE CASCADE;
