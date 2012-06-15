use study;
UPDATE `study`.`phone_type` SET `NAME`='Home' WHERE `ID`='2';
UPDATE `study`.`phone_type` SET `NAME`='Work' WHERE `ID`='3';

DELETE FROM `study`.`phone_type` WHERE `ID`='4';

UPDATE `study`.`phone_type` SET `DESCRIPTION`='Mobile  Cell Phones' WHERE `ID`='1';
UPDATE `study`.`phone_type` SET `DESCRIPTION`='Land Home Phone' WHERE `ID`='2';
UPDATE `study`.`phone_type` SET `DESCRIPTION`='Land Phone at Office' WHERE `ID`='3';
