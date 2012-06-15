CREATE  TABLE `study`.`payload` (
  `id` INT NOT NULL ,
  `payload` BLOB NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'This is strictly for storing large objects, and will slightly aid in having hibernate lazy loading work accross databases';

