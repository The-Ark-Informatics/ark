use `config`;

ALTER TABLE `config_fields` ADD `TYPE` INT(11)  NOT NULL  AFTER `DESCRIPTION`;

alter table config_fields
add constraint fk_field_type
foreign key (`type`)
references `study`.`field_type`(ID);