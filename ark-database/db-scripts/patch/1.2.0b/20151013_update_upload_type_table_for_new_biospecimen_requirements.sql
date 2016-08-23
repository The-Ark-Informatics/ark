set @ID1=(SELECT ID FROM `study`.`upload_type` where `NAME`='Biospecimen Location Updater');
set @ID2=(SELECT ID FROM `study`.`upload_type` where `NAME`='Biospecimen Custom Data');
set @ID3=(SELECT ID FROM `study`.`upload_type` where `NAME`='Biocollection Custom Data');
UPDATE `study`.`upload_type` SET `NAME`='Biospecimen Inventory' WHERE `ID`=@ID1;
UPDATE `study`.`upload_type` SET `NAME`='Biospecimen', `DESCRIPTION`='Biospecimen deails' WHERE `ID`=@ID2;
UPDATE `study`.`upload_type` SET `NAME`='Biocollection',`DESCRIPTION`='Biocollection deails' WHERE `ID`=@ID3;

ALTER TABLE `study`.`upload_type` 
ADD COLUMN `ORDER` INT(11) NULL AFTER `ARK_MODULE_ID`;

UPDATE `study`.`upload_type` SET `ORDER`='1' WHERE `ID`=@ID3;
UPDATE `study`.`upload_type` SET `ORDER`='2' WHERE `ID`=@ID1;
UPDATE `study`.`upload_type` SET `ORDER`='3' WHERE `ID`=@ID2;

