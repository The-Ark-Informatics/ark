CREATE TABLE `spark`.`analysis` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NULL,
  `DESCRIPTION` VARCHAR(255) NULL,
  `STATUS` VARCHAR(25) NULL,
  `MICRO_SERVICE_ID` INT(11) NULL,
  `DATA_SOURCE_ID` INT(11) NULL,
  `COMPUTATION_ID` INT(11) NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_analysis_micro_service_id_idx` (`MICRO_SERVICE_ID` ASC),
  INDEX `fk_analysis_data_source_id_idx` (`DATA_SOURCE_ID` ASC),
  INDEX `fk_analysis_computation_id_idx` (`COMPUTATION_ID` ASC),
  CONSTRAINT `fk_analysis_micro_service_id`
    FOREIGN KEY (`MICRO_SERVICE_ID`)
    REFERENCES `spark`.`micro_service` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_analysis_data_source_id`
    FOREIGN KEY (`DATA_SOURCE_ID`)
    REFERENCES `spark`.`data_source` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_analysis_computation_id`
    FOREIGN KEY (`COMPUTATION_ID`)
    REFERENCES `spark`.`computation` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;




