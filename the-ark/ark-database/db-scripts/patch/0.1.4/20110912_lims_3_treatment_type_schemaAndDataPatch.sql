USE lims;

CREATE  TABLE `lims`.`treatment_type` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(100) NOT NULL ,
  PRIMARY KEY (`ID`) ,
  UNIQUE INDEX `NAME_UNIQUE` (`NAME` ASC) )
ENGINE = InnoDB;


INSERT INTO `lims`.`treatment_type` (`NAME`) VALUES ('Frozen');
INSERT INTO `lims`.`treatment_type` (`NAME`) VALUES ('Formalin Fixed');
INSERT INTO `lims`.`treatment_type` (`NAME`) VALUES ('Tissue Cultured');
INSERT INTO `lims`.`treatment_type` (`NAME`) VALUES ('70% Alcohol Fixed');
INSERT INTO `lims`.`treatment_type` (`NAME`) VALUES ('RN later');
INSERT INTO `lims`.`treatment_type` (`NAME`) VALUES ('RNA later, then Formalin Fixed');
INSERT INTO `lims`.`treatment_type` (`NAME`) VALUES ('RNA later, then Snap Frozen');
INSERT INTO `lims`.`treatment_type` (`NAME`) VALUES ('Unprocessed');
