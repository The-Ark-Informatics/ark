USE lims;
ALTER TABLE `inv_cell` DROP FOREIGN KEY `fk_inv_cell_box`;
ALTER TABLE `inv_cell`  ADD CONSTRAINT `fk_inv_cell_box` FOREIGN KEY (`BOX_ID`) REFERENCES `inv_box` (`ID`) ON UPDATE NO ACTION ON DELETE CASCADE;
