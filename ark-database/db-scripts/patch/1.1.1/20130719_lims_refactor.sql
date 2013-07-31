drop table if exists lims.appointments	;
drop table if exists lims.attachment	;
drop table if exists lims.barcode_command	;
drop table if exists lims.barcode_printer	;
drop table if exists lims.barcodeformat	;
drop table if exists lims.barcodeid_engine ;
drop table if exists lims.biodata	;
drop table if exists lims.biodata_field_group	;
drop table if exists lims.biodata_field_lov	;
drop table if exists lims.biodata_lov_list	;
drop table if exists lims.biodata_type	;
drop table if exists lims.biodata_unit	;
drop table if exists lims.biodata_field	;
drop table if exists lims.flag	;
drop table if exists lims.group	;
drop table if exists lims.inv_type	;
drop table if exists lims.listofvalues	;
drop table if exists lims.listofvalues_description	;
drop table if exists lims.note	;
drop table if exists lims.samplecode	;
drop table if exists lims.biodata_group_criteria	;
drop table if exists lims.biodata_criteria	;
drop table if exists lims.biodata_group	;
#  don't delete even though empty...this links to biotransaction...may be used?  drop table if exists lims.access_request;



ALTER TABLE `lims`.`unit` 
ADD COLUMN `FACTOR` DOUBLE NOT NULL DEFAULT 1  AFTER `DESCRIPTION` , 
ADD COLUMN `ORDER` BIGINT NOT NULL DEFAULT 1  AFTER `FACTOR` , 
ADD COLUMN `TYPE` ENUM('VOLUME', 'MASS', 'DISTANCE', 'TIME','TEMPERATURE','UNKNOWN') NOT NULL DEFAULT 'UNKNOWN'  AFTER `ORDER` ;

ALTER TABLE `lims`.`unit` CHANGE COLUMN `TYPE` `TYPE` ENUM('VOLUME','MASS','TIME','TEMPERATURE','DISTANCE','UNKNOWN') NOT NULL DEFAULT 'UNKNOWN'  ;


UPDATE `lims`.`unit` SET `NAME`='ul', `ORDER`='3', `TYPE`='VOLUME' WHERE `ID`='2';
UPDATE `lims`.`unit` SET `NAME`='nl', `FACTOR`='0.001', `ORDER`='2', `TYPE`='VOLUME' WHERE `ID`='3';
UPDATE `lims`.`unit` SET `TYPE`='DISTANCE' WHERE `ID`='1';
UPDATE `lims`.`unit` SET `FACTOR`='1000000', `ORDER`='5', `TYPE`='VOLUME' WHERE `ID`='38';
UPDATE `lims`.`unit` SET `FACTOR`='1000', `ORDER`='4' WHERE `ID`='17';


UPDATE `lims`.`unit` SET `NAME`='g', `ORDER`='104', `TYPE`='MASS' WHERE `ID`='12';
UPDATE `lims`.`unit` SET `NAME`='mg', `ORDER`='103', `TYPE`='MASS' WHERE `ID`='15';
UPDATE `lims`.`unit` SET `ORDER`='105', `TYPE`='MASS' WHERE `ID`='10';
UPDATE `lims`.`unit` SET `NAME`='ug', `ORDER`='102', `TYPE`='MASS' WHERE `ID`='14';
UPDATE `lims`.`unit` SET `ORDER`='301', `TYPE`='DISTANCE' WHERE `ID`='16';
UPDATE `lims`.`unit` SET `TYPE`='VOLUME' WHERE `ID`='17';
UPDATE `lims`.`unit` SET `ORDER`='301', `TYPE`='DISTANCE' WHERE `ID`='1';
UPDATE `lims`.`unit` SET `ORDER`='-1' WHERE `ID`='0';

UPDATE `lims`.`unit` SET `FACTOR`='1000000' WHERE `ID`='10';
UPDATE `lims`.`unit` SET `FACTOR`='1000' WHERE `ID`='12';
UPDATE `lims`.`unit` SET `FACTOR`='0.001' WHERE `ID`='14';
UPDATE `lims`.`unit` SET `FACTOR`='.001' WHERE `ID`='1';







