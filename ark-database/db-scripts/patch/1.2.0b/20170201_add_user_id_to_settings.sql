ALTER TABLE `config`.`settings` ADD `user_id` INT(11)  NULL  DEFAULT NULL  AFTER `study_id`;
ALTER TABLE `config`.`aud_settings` ADD `user_id` INT(11)  NULL  DEFAULT NULL  AFTER `study_id`;
