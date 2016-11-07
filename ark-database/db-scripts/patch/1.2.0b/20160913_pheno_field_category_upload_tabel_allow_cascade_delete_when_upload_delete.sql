use `pheno`;
-- ALTER TABLE `pheno_field_category_upload` 
-- DROP FOREIGN KEY `FK_PHENO_FIELD_UPLOAD_ID`;
ALTER TABLE `pheno_field_category_upload` 
ADD CONSTRAINT `FK_PHENO_FIELD_UPLOAD_ID`
  FOREIGN KEY (`UPLOAD_ID`)
  REFERENCES `study`.`upload` (`ID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
