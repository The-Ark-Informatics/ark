CREATE  TABLE `study`.`upload_status` (
  `id` INT NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `long_message` VARCHAR(45) NOT NULL COMMENT 'evenutally messages may all be brought out to allow i18n (internationalization)' ,
  `short_message` VARCHAR(45) NOT NULL COMMENT 'evenutally messages may all be brought out to allow i18n (internationalization)' ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) );

