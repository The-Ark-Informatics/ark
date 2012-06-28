# drop table study.payload;

CREATE  TABLE `study`.`payload` (
  `id` INT(11) NOT NULL ,
  `payload` BLOB NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'This is a simple table for storing LObs and an id to represent them.  Hopefully pushing heavy stuff outside of tables to which logic is applied and making hibernate behave and not do too much heavy lifting until we need it too in more databases (think \"lazy loading\", hibernate doesnt always listen to lazy loading of an attribute...thus make it another entity with lazy loading).  ';

ALTER TABLE `study`.`upload` ADD COLUMN `PAYLOAD_ID` INT(11) NULL  AFTER `UPLOAD_TYPE_ID` , 
  ADD CONSTRAINT `fk_upload_payload`
  FOREIGN KEY (`PAYLOAD_ID` )
  REFERENCES `study`.`payload` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_upload_payload` (`PAYLOAD_ID` ASC) ;


