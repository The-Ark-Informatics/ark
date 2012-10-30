-- ARK
ALTER TABLE `lims`.`study_inv_site` DROP FOREIGN KEY `fk_study_inv_site_inv_site` ;
ALTER TABLE `lims`.`study_inv_site` 
  ADD CONSTRAINT `fk_study_inv_site_inv_site`
  FOREIGN KEY (`INV_SITE_ID` )
  REFERENCES `lims`.`inv_site` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;