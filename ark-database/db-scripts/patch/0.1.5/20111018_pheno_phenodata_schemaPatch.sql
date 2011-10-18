USE pheno;

ALTER TABLE `pheno`.`pheno_data` DROP FOREIGN KEY `FK_PHENO_DATA_PHENO_COLLECTION_ID` ;

-- This CASCADE DELETE when linked pheno_collection row is being removed
ALTER TABLE `pheno`.`pheno_data` 
  ADD CONSTRAINT `FK_PHENO_DATA_PHENO_COLLECTION_ID`
  FOREIGN KEY (`PHENO_COLLECTION_ID` )
  REFERENCES `pheno`.`pheno_collection` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;