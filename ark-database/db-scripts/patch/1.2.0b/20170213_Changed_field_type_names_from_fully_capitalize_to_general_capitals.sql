
SET @Character_id=(SELECT ID FROM `study`.`field_type` where name='CHARACTER');
SET @Number_id=(SELECT ID FROM `study`.`field_type` where name='NUMBER');
SET @Date_id=(SELECT ID FROM `study`.`field_type` where name='DATE');
SET @Lookup_id=(SELECT ID FROM `study`.`field_type` where name='LOOKUP');

UPDATE `study`.`field_type` SET `NAME`='Character' WHERE `ID`=@Character_id;
UPDATE `study`.`field_type` SET `NAME`='Number' WHERE `ID`=@Number_id;
UPDATE `study`.`field_type` SET `NAME`='Date' WHERE `ID`=@Date_id;
UPDATE `study`.`field_type` SET `NAME`='LookUp' WHERE `ID`=@Lookup_id;

