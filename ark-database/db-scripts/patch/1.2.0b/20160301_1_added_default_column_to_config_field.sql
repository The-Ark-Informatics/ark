ALTER TABLE `config`.`config_fields` ADD `DEFAULT_VALUE` VARCHAR(100)  NULL  DEFAULT NULL  AFTER `TYPE`;

UPDATE `config`.`config_fields` SET `DEFAULT_VALUE` = '20';


