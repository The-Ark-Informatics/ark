use study;
ALTER TABLE `study`.`ark_function` ADD COLUMN `RESOURCE_KEY` VARCHAR(255) NULL  AFTER `ARK_FUNCTION_TYPE_ID` ;

/* Data Patch is placed here to issues in maintain the sequence. Any other data patch following this can be independent of the schema patch.*/
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.study.details' WHERE `ID`='1';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.study.components' WHERE `ID`='2';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.mydetails' WHERE `ID`='3';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.user.management' WHERE `ID`='4';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.subject.detail' WHERE `ID`='5';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.person.phone' WHERE `ID`='6';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.person.address' WHERE `ID`='7';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.subject.subjectFile' WHERE `ID`='8';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.subject.consent' WHERE `ID`='9';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.subject.subjectUpload' WHERE `ID`='10';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.subject.subjectcustom' WHERE `ID`='11';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.phenotypic.field' WHERE `ID`='12';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.phenotypic.fieldUpload' WHERE `ID`='13';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.phenotypic.collection' WHERE `ID`='14';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.phenotypic.fieldData' WHERE `ID`='15';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.phenotypic.phenoUpload' WHERE `ID`='16';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.lims.subject.detail' WHERE `ID`='17';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.lims.collection' WHERE `ID`='18';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.lims.biospecimen' WHERE `ID`='19';
UPDATE `study`.`ark_function` SET `RESOURCE_KEY`='tab.module.lims.inventory' WHERE `ID`='20';

INSERT INTO `study`.`ark_function` (`ID`, `NAME`, `DESCRIPTION`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES (21, 'CORRESPONDENCE', '', 1, 'tab.module.subject.correspondence');








