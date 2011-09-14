USE lims;

CREATE  TABLE `lims`.`bio_transaction_status` (
  `ID` INT NOT NULL AUTO_INCREMENT ,
  `NAME` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`ID`) ,
  UNIQUE INDEX `NAME_UNIQUE` (`NAME` ASC) )
ENGINE = InnoDB;

INSERT INTO `lims`.`bio_transaction_status` (`NAME`) VALUES ('Initial Qty');
INSERT INTO `lims`.`bio_transaction_status` (`NAME`) VALUES ('Pending');
INSERT INTO `lims`.`bio_transaction_status` (`NAME`) VALUES ('Completed');
