DROP TABLE IF EXISTS `geno`.`command`;
CREATE  TABLE `geno`.`command` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(255) NULL ,
  `LOCATION` VARCHAR(255) NULL ,
  `SERVER_URL` VARCHAR(255) NULL ,
  `INPUT_FILE_FORMAT` VARCHAR(255) NULL ,
  `OUTPUT_FILE_FORMAT` VARCHAR(255) NULL COMMENT 'may need error output too i guess or alt output' ,
  PRIMARY KEY (`ID`) )  ENGINE=InnoDB ;

DROP TABLE IF EXISTS `geno`.`process`;
CREATE  TABLE `geno`.`process` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(255) NULL ,
  `DESCRIPTION` VARCHAR(4096) NULL ,
  `INPUT_FILE_LOCATION` VARCHAR(255) NULL ,
  `INPUT_FILE_HASH` VARCHAR(255) NULL ,
  `INPUT_FILE_TYPE` VARCHAR(255) NULL ,
  `INPUT_KEPT` BOOLEAN NULL ,
  `INPUT_SERVER` VARCHAR(255) NULL ,
  `OUTPUT_FILE_LOCATION` VARCHAR(255) NULL ,
  `OUTPUT_FILE_HASH` VARCHAR(255) NULL ,
  `OUTPUT_FILE_TYPE` VARCHAR(255) NULL ,
  `OUTPUT_KEPT` BOOLEAN NULL ,
  `OUTPUT_SERVER` VARCHAR(255) NULL ,
  `COMMAND_ID` INT NULL COMMENT 'The command is the task/program that will perform the process/transform' ,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;

DROP TABLE IF EXISTS `geno`.`transformation_template`;
CREATE  TABLE `geno`.`transformation_template` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(255) NOT NULL ,
  `DESCRIPTION` VARCHAR(4096) NULL ,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;

/**where do I link a person**/
DROP TABLE IF EXISTS `geno`.`transformation`;
CREATE  TABLE `geno`.`transformation` (
  `ID` INT NOT NULL ,
  `NAME` VARCHAR(255) NOT NULL ,
  `DESCRIPTION` VARCHAR(4096) NULL ,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;

DROP TABLE IF EXISTS `geno`.`transformation_process`;
CREATE  TABLE `geno`.`transformation_process` (
  `ID` INT NOT NULL ,
  `TRANSFORMATION_ID` INT NOT NULL,
  `PROCESS_ID` INT NOT NULL,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;

DROP TABLE IF EXISTS `geno`.`lss_process`;
/* really could be person*/
CREATE  TABLE `geno`.`lss_process` (
  `ID` INT NOT NULL ,
  `TRANSFORMATION_ID` INT NOT NULL,
  `LSS_ID` INT NOT NULL,
  PRIMARY KEY (`ID`) ) ENGINE=InnoDB ;







