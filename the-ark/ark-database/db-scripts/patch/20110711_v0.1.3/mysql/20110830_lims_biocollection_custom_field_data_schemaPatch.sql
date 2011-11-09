USE lims;

-- Ensures that if the bioCollection is deleted, then the associated biocollectionCustomFieldData is also deleted.
ALTER TABLE `lims`.`biocollection_custom_field_data` DROP FOREIGN KEY `FK_BIOCOLCFDATA_BIOCOLLECTION_ID` ;
ALTER TABLE `lims`.`biocollection_custom_field_data` 
  ADD CONSTRAINT `FK_BIOCOLCFDATA_BIOCOLLECTION_ID`
  FOREIGN KEY (`BIO_COLLECTION_ID` )
  REFERENCES `lims`.`collection` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

