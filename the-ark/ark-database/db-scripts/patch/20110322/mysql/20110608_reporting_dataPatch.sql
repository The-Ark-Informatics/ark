USE reporting;

-- UPDATE Study-level Consent Details Report to be attached to Study module
UPDATE `reporting`.`report_template` SET `MODULE_ID`=1 WHERE `ID`=1;

-- UPDATE Study-level Consent Details Report to be attached to Subject module
UPDATE `reporting`.`report_template` SET `MODULE_ID`=2 WHERE `ID`=2;

-- UPDATE Study-comp Consent Details Report to be attached to Subject module
UPDATE `reporting`.`report_template` SET `MODULE_ID`=2 WHERE `ID`=3;

-- INSERT new Pheno Field Details Report and attach it to Pheno module
INSERT INTO `report_template`
(ID,NAME,DESCRIPTION,TEMPLATE_PATH,MODULE_ID,FUNCTION_ID) 
VALUES 
(4,'Phenotypic Field Details Report (Data Dictionary)', 'This report lists detailed field information for a particular study based on their associated phenotypic collection.', 'DataDictionaryReport.jrxml', 3, NULL);
