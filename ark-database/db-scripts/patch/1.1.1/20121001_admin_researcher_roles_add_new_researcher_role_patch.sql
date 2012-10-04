USE admin;

INSERT INTO `admin`.`researcher_role` (`NAME`) VALUES ('Principal Investigator');
UPDATE `admin`.`researcher_role` SET ID=0 WHERE NAME = 'Principal Investigator';
