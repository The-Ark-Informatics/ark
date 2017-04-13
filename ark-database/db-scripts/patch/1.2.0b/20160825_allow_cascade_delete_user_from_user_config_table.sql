ALTER TABLE `config`.`user_config` 
DROP FOREIGN KEY `user_config_ibfk_3`;
ALTER TABLE `config`.`user_config` 
ADD CONSTRAINT `user_config_ibfk_3`
  FOREIGN KEY (`USER_ID`)
  REFERENCES `study`.`ark_user` (`ID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
