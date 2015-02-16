ALTER TABLE `admin`.`researcher_role` 
ADD COLUMN `ORDER_ID` INT(11) NOT NULL DEFAULT 0 AFTER `DESCRIPTION`;

UPDATE `admin`.`researcher_role` SET `ORDER_ID`='1' WHERE `NAME`='Principal Investigator';
UPDATE `admin`.`researcher_role` SET `ORDER_ID`='2' WHERE `NAME`='Chief Investigator';
UPDATE `admin`.`researcher_role` SET `ORDER_ID`='3' WHERE `NAME`='Associate Investigator';
UPDATE `admin`.`researcher_role` SET `ORDER_ID`='4' WHERE `NAME`='Research Assistant';
UPDATE `admin`.`researcher_role` SET `ORDER_ID`='5' WHERE `NAME`='Other Investigator';
