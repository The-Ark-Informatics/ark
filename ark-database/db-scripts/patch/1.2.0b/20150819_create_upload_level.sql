CREATE TABLE `study`.`upload_level` (
  `id` INT NOT NULL,
  `name` VARCHAR(20) NOT NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `study`.`upload_level` 
CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;


INSERT INTO `study`.`upload_level` (`name`, `description`) VALUES ('Field', 'Field level custom field upload. ');
INSERT INTO `study`.`upload_level` (`name`, `description`) VALUES ('Category', 'Category level custom field upload.');
