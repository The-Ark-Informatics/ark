set @ID1=(SELECT ID FROM `config`.`config_fields` where `NAME`='ROWS_PER_PAGE');
set @ID2=(SELECT ID FROM `config`.`config_fields` where `NAME`='CUSTOM_FIELDS_PER_PAGE');

set @typeid=(SELECT ID FROM `study`.`field_type` where `NAME`='NUMBER'); ;

UPDATE `config`.`config_fields` SET `TYPE`='2' WHERE `ID`=@ID1;
UPDATE `config`.`config_fields` SET `TYPE`='2' WHERE `ID`=@ID2;

-- Add constrain to avoid having unnecessary type ids -------------- 

ALTER TABLE `config`.`config_fields` 
ADD INDEX `fk_config_fields_type_idx` (`TYPE` ASC);
ALTER TABLE `config`.`config_fields` 
ADD CONSTRAINT `fk_config_fields_type`
  FOREIGN KEY (`TYPE`)
  REFERENCES `study`.`field_type` (`ID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

