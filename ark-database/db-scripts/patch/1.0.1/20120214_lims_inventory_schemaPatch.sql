USE lims;
/* Rename tank to freezer */
ALTER TABLE `inv_tank`
	DROP FOREIGN KEY `fk_inv_tank_site`;
ALTER TABLE `inv_tank`
	RENAME TO `inv_freezer`,
	DROP INDEX `fk_inv_tank_site`,
	ADD INDEX `fk_inv_freezer_site` (`SITE_ID`),
	ADD CONSTRAINT `fk_inv_freezer_site` FOREIGN KEY (`SITE_ID`) REFERENCES `inv_site` (`ID`) ON UPDATE NO ACTION ON DELETE CASCADE;

/* Rename tray to rack */
ALTER TABLE `inv_tray` RENAME TO `inv_rack`;
ALTER TABLE `inv_rack` DROP FOREIGN KEY `fk_inv_tray_tank` ;
ALTER TABLE `inv_rack` CHANGE COLUMN `TANK_ID` `FREEZER_ID` INT(11) NOT NULL  , 
  ADD CONSTRAINT `fk_inv_tray_tank`
  FOREIGN KEY (`FREEZER_ID` )
  REFERENCES `inv_tank` (`ID` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, DROP INDEX `fk_inv_tank_tray_idx` 
, ADD INDEX `fk_inv_tank_tray_idx` USING BTREE (`FREEZER_ID` ASC) ;
ALTER TABLE `inv_rack` DROP FOREIGN KEY `fk_inv_tray_tank` ;
ALTER TABLE `inv_rack` 
  ADD CONSTRAINT `fk_inv_rack_freezer`
  FOREIGN KEY (`FREEZER_ID` )
  REFERENCES `inv_tank` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION
, DROP INDEX `fk_inv_tank_tray_idx` 
, ADD INDEX `fk_inv_rack_freezer_idx` USING BTREE (`FREEZER_ID` ASC) ;

/* Rename reference of tray to rack */
ALTER TABLE `lims`.`inv_box` DROP FOREIGN KEY `fk_inv_box_tray` ;
ALTER TABLE `lims`.`inv_box` CHANGE COLUMN `TRAY_ID` `RACK_ID` INT(11) NOT NULL  , 
  ADD CONSTRAINT `fk_inv_box_rack`
  FOREIGN KEY (`RACK_ID` )
  REFERENCES `lims`.`inv_rack` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION
, DROP INDEX `fk_inv_box_tray_idx` 
, ADD INDEX `fk_inv_box_rack_idx` (`RACK_ID` ASC) ;
