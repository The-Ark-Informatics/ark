/*
 * Emergency data patch to fix functionality in ark-0.1.4-SNAPSHOT.war
 * (i.e. these data patches do not require any code changes)
 */

USE study;

-- Update all the so that the Description is actually populated (Fixes ARK-327)
UPDATE `study`.`unit_type` SET `DESCRIPTION`='Millimetres (mm)' WHERE `ID`='1';
UPDATE `study`.`unit_type` SET `DESCRIPTION`='Centimetres (cm)' WHERE `ID`='2';
UPDATE `study`.`unit_type` SET `DESCRIPTION`='Metres (m)' WHERE `ID`='3';
UPDATE `study`.`unit_type` SET `DESCRIPTION`='Grams (g)' WHERE `ID`='4';
UPDATE `study`.`unit_type` SET `DESCRIPTION`='Kilograms (kg)' WHERE `ID`='5';
UPDATE `study`.`unit_type` SET `DESCRIPTION`='Litres (L)' WHERE `ID`='6';
UPDATE `study`.`unit_type` SET `DESCRIPTION`='Hours (hrs)' WHERE `ID`='10';
UPDATE `study`.`unit_type` SET `DESCRIPTION`='Minutes (min)' WHERE `ID`='11';
UPDATE `study`.`unit_type` SET `DESCRIPTION`='Seconds (s)' WHERE `ID`='12';
UPDATE `study`.`unit_type` SET `DESCRIPTION`=`NAME` WHERE `DESCRIPTION` IS NULL OR `DESCRIPTION`='';

-- Prevent future updates from inserting rows without descriptions.
ALTER TABLE `study`.`unit_type` CHANGE COLUMN `DESCRIPTION` `DESCRIPTION` VARCHAR(255) NOT NULL  ;
