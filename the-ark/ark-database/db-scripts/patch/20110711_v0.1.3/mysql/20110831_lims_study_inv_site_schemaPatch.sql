CREATE  TABLE `lims`.`study_inv_site` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT ,
  `STUDY_ID` INT(11) NOT NULL ,
  `INV_SITE_ID` INT(11) NOT NULL ,
  PRIMARY KEY (`ID`) ,
  INDEX `fk_study_inv_site_study` (`STUDY_ID` ASC) ,
  INDEX `fk_study_inv_site_inv_site` (`INV_SITE_ID` ASC) ,
  CONSTRAINT `fk_study_inv_site_study`
    FOREIGN KEY (`STUDY_ID` )
    REFERENCES `study`.`study` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_study_inv_site_inv_site`
    FOREIGN KEY (`INV_SITE_ID` )
    REFERENCES `lims`.`inv_site` (`ID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
