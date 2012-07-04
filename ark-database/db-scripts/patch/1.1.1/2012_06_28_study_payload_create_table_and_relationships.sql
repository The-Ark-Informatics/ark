use study;
drop table if exists study.payload;

-- Hopefully pushing heavy stuff outside of tables to which logic is applied and making hibernate behave and not do too much heavy lifting until 
-- we need it too in more databases (think \"lazy loading\", hibernate doesnt always listen to lazy loading of an attribute...thus make it another entity with lazy loading).  
CREATE  TABLE `study`.`payload` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `PAYLOAD` LONGBLOB NOT NULL ,
  PRIMARY KEY (`ID`) )
ENGINE = InnoDB
COMMENT = 'This is a simple table for storing LOBs and an id to represent them.';

ALTER TABLE `study`.`upload` ADD COLUMN `PAYLOAD_ID` INT(11) NULL  AFTER `UPLOAD_TYPE_ID` , 
  ADD CONSTRAINT `fk_upload_payload`
  FOREIGN KEY (`PAYLOAD_ID` )
  REFERENCES `study`.`payload` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_upload_payload` (`PAYLOAD_ID` ASC) ;

-- then fix the data
insert into study.payload(ID, PAYLOAD) 
select up.ID, up.PAYLOAD from study.upload up;

update study.upload set PAYLOAD_ID = ID ;

-- then drop the old blob column from 
alter table study.upload drop column PAYLOAD;

-- then force payload_id to be not null?
ALTER TABLE `study`.`upload` DROP FOREIGN KEY `fk_upload_payload` ;
ALTER TABLE `study`.`upload` CHANGE COLUMN `PAYLOAD_ID` `PAYLOAD_ID` INT(11) NOT NULL  , 
  ADD CONSTRAINT `fk_upload_payload`
  FOREIGN KEY (`PAYLOAD_ID` )
  REFERENCES `study`.`payload` (`id` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;