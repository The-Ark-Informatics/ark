USE lims;

CREATE TABLE `inv_col_row_type` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;

INSERT INTO `inv_col_row_type`(id, name) VALUES (1, 'Numeric');
INSERT INTO `inv_col_row_type`(id, name) VALUES (2, 'Alphabet');-- Remove Collection function from LIMS module
DELETE FROM `study`.`ark_module_function` WHERE ID =18;  

ALTER TABLE `inv_box` DROP FOREIGN KEY `fk_inv_tray_box`;
ALTER TABLE `inv_box` DROP INDEX `fk_inv_tray_box_idx`;
ALTER TABLE `inv_box` MODIFY COLUMN `NAME`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `TIMESTAMP`;
ALTER TABLE `inv_box` MODIFY COLUMN `NOOFCOL`  int(11) NOT NULL AFTER `NAME`;
ALTER TABLE `inv_box` MODIFY COLUMN `CAPACITY`  int(11) NULL DEFAULT NULL AFTER `NOOFCOL`;
ALTER TABLE `inv_box` MODIFY COLUMN `TRAY_ID`  int(11) NOT NULL AFTER `CAPACITY`;
ALTER TABLE `inv_box` ADD COLUMN `COLNOTYPE_ID`  int(11) NOT NULL AFTER `NOOFROW`;
ALTER TABLE `inv_box` ADD COLUMN `ROWNOTYPE_ID`  int(11) NOT NULL AFTER `COLNOTYPE_ID`;
ALTER TABLE `inv_box` MODIFY COLUMN `TRANSFER_ID`  int(11) NULL DEFAULT NULL ;
ALTER TABLE `inv_box` DROP COLUMN `COLNOTYPE`;
ALTER TABLE `inv_box` DROP COLUMN `ROWNOTYPE`;
ALTER TABLE `inv_box` ADD CONSTRAINT `fk_inv_box_rowtype` FOREIGN KEY (`ROWNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `fk_inv_box_rowtype_idx` (`ROWNOTYPE_ID`);
ALTER TABLE `inv_box` ADD CONSTRAINT `fk_inv_box_coltype` FOREIGN KEY (`COLNOTYPE_ID`) REFERENCES `inv_col_row_type` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `fk_inv_box_coltype_idx` (`COLNOTYPE_ID`);
ALTER TABLE `inv_box` ADD CONSTRAINT `fk_inv_box_tray` FOREIGN KEY (`TRAY_ID`) REFERENCES `inv_tray` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION, ADD INDEX `fk_inv_box_tray_idx` (`TRAY_ID`);

ALTER TABLE `biospecimen` DROP FOREIGN KEY `fk_biospecimen_inv_cell`;
ALTER TABLE `biospecimen` DROP INDEX `fk_biospecimen_inv_cell`;
ALTER TABLE `biospecimen` MODIFY COLUMN `DEPTH`  int(11) NULL DEFAULT NULL AFTER `GRADE`;
ALTER TABLE `biospecimen` MODIFY COLUMN `UNITS`  varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `QUANTITY`;
ALTER TABLE `biospecimen` ADD COLUMN `BARCODED`  tinyint(1) NULL DEFAULT 0 AFTER `TREATMENT`;
ALTER TABLE `biospecimen` DROP COLUMN `CELL_ID`;


ALTER TABLE `inv_cell` DROP FOREIGN KEY `fk_inv_cell_tray`;
ALTER TABLE `inv_cell` DROP INDEX `fk_inv_cell_tray_idx`;
ALTER TABLE `inv_cell` ADD COLUMN `BIOSPECIMEN_ID`  int(11) NULL DEFAULT NULL AFTER `STATUS`;
ALTER TABLE `inv_cell` ADD CONSTRAINT `fk_inv_cell_biospecimen` FOREIGN KEY (`BIOSPECIMEN_ID`) REFERENCES `biospecimen` (`ID`) ON DELETE SET NULL ON UPDATE NO ACTION;
ALTER TABLE `inv_cell` ADD CONSTRAINT `fk_inv_cell_box` FOREIGN KEY (`BOX_ID`) REFERENCES `inv_box` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
CREATE INDEX `fk_inv_cell_box_idx` USING BTREE ON `inv_cell`(`BOX_ID`);
CREATE INDEX `fk_inv_cell_biospecimen_idx` USING BTREE ON `inv_cell`(`BIOSPECIMEN_ID`);

ALTER TABLE `inv_tray` ADD CONSTRAINT `fk_inv_tray_tank` FOREIGN KEY (`TANK_ID`) REFERENCES `inv_tank` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
CREATE INDEX `fk_inv_tray_tank_idx` USING BTREE ON `inv_tray`(`TANK_ID`);
ALTER TABLE `inv_tray` DROP FOREIGN KEY `fk_inv_box_tank`;
ALTER TABLE `inv_tray` DROP INDEX `fk_inv_box_tank_idx`;

CREATE TABLE `inv_type` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
`DESCRIPTION`  text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;