INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) 
VALUES (58, 'BIOSPECIMEN_AND_BIOCOLLECTION_CUSTOM_FIELD_UPLOAD', 'Uploader for both Biospecimen and Biocollection Custom Fields', 1, 'tab.module.lims.bioupload');


UPDATE `study`.`upload_type` SET `DESCRIPTION`='Custom Data to be associated with a biospecimen', `ARK_MODULE_ID`=5 WHERE `ID`='0';

INSERT INTO `study`.`upload_type` (`ID`, `NAME`, `DESCRIPTION`, `ARK_MODULE_ID`) VALUES (4, 'Biocollection Custom Data', 'Custom Data to be associated with a biospecimen', 5);

--note ark_module_function done via web interface...or should we default to having it there and they can use webface to remove it?

