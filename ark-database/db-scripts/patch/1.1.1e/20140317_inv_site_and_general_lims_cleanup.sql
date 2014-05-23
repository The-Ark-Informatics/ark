-- todo cleanup
select * from lims.inv_site;


-- Chris made this many to many a while back
ALTER TABLE `lims`.`inv_site` DROP FOREIGN KEY `FK_INV_SITE_STUDY` ;
ALTER TABLE `lims`.`inv_site` DROP COLUMN `STUDY_ID` 
, DROP INDEX `FK_INV_SITE_STUDY` ;

-- Trav missed this in making transaction unit changes...need to relate to a unit - 
ALTER TABLE `lims`.`bio_transaction` 
  ADD CONSTRAINT `FK_BIOTRANSACTION_UNIT_IT`
  FOREIGN KEY (`UNIT_ID` )
  REFERENCES `lims`.`unit` (`ID` )
  ON DELETE RESTRICT
  ON UPDATE CASCADE
, ADD INDEX `FK_BIOTRANSACTION_UNIT_IT_idx` (`UNIT_ID` ASC) ;


-- stops repeat boxes in a rack
alter table lims.inv_box add unique index (name, rack_id);


-- stops repeat rows
alter table lims.inv_cell add unique index (box_id, rowno, colno);


ALTER TABLE `lims`.`inv_site` 
ADD UNIQUE INDEX `NAME_UNIQUE` (`NAME` ASC) ;


select count(*) from inv_cell ;

select count(*) from wagerlab.ix_inv_cell where biospecimenkey is null or biospecimenkey < 1;
