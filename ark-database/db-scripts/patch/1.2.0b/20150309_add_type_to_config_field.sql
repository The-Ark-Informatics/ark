
ALTER TABLE `config`.`config_fields` ADD `TYPE` INT(11)  NOT NULL  AFTER `DESCRIPTION`;

set @id_row=(SELECT id FROM config.config_fields where name='ROWS_PER_PAGE');
set @id_fields=(SELECT id FROM config.config_fields where name='CUSTOM_FIELDS_PER_PAGE');

UPDATE `config`.`config_fields` SET `TYPE`=(SELECT id FROM study.field_type where name='NUMBER') WHERE `ID`=@id_row;
UPDATE `config`.`config_fields` SET `TYPE`=(SELECT id FROM study.field_type where name='NUMBER') WHERE `ID`=@id_fields;


alter table config_fields add constraint fk_field_type foreign key (`type`777) references `study`.`field_type`(ID);
