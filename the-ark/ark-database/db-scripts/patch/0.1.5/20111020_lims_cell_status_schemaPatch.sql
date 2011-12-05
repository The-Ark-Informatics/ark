USE lims;

CREATE TABLE `cell_status` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` text,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO lims.cell_status(name, description) VALUES('Empty', 'Cell is empty and available');
INSERT INTO lims.cell_status(name, description) VALUES('Used', 'Cell is used and unavailable');
INSERT INTO lims.cell_status(name, description) VALUES('Held', 'Cell is held for allocation');