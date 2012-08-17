-- Rename the researcher table column institute to organization

ALTER TABLE `admin`.`researcher` CHANGE COLUMN `INSTITUTE` `ORGANIZATION` VARCHAR(50) NULL DEFAULT NULL  ;

-- Additional changes for the billable_item and researcher tables

ALTER TABLE `admin`.`bilable_item` ADD COLUMN `TOTAL_COST` DOUBLE NULL AFTER `BILLABLE_TYPE`;

ALTER TABLE `admin`.`bilable_item` ADD COLUMN `ITEM_COST` DOUBLE NULL  AFTER `TOTAL_COST` ;

ALTER TABLE `admin`.`bilable_item` ADD COLUMN `ATTACHMENT_FILENAME` VARCHAR(45) NULL  AFTER `TOTAL_COST` , ADD COLUMN `ATTACHMENT_PAYLOAD` LONGBLOB NULL  AFTER `ATTACHMENT_FILENAME` ;

ALTER TABLE `admin`.`researcher` ADD COLUMN `TITLE_TYPE_ID` INT(11) NULL  AFTER `STUDY_ID` ;

ALTER TABLE `admin`.`bilable_item` ADD COLUMN `TOTAL_GST` DOUBLE NULL  AFTER `ATTACHMENT_PAYLOAD` ;

-- Add the Researcher Recruitment Costs - Summary of Account report
INSERT INTO `reporting`.`report_template`
(`NAME`,
`DESCRIPTION`,
`TEMPLATE_PATH`,
`MODULE_ID`,
`FUNCTION_ID`)
VALUES
(
'Work Researcher Cost Report',
'This report lists the total billable item type costs realated to a researcher',
'ResearcherCostReport.jrxml',
(select ID from study.ark_module where name='Work Tracking'),
(select ID from study.ark_function where name='BILLABLE_ITEM')
);

-- Rename the Work module to Work Tracking
UPDATE `study`.`ark_module` SET `NAME`='Work Tracking' WHERE `NAME`='Work'

-- Researcher detail cost report
INSERT INTO `reporting`.`report_template` (`NAME`, `DESCRIPTION`, `TEMPLATE_PATH`, `MODULE_ID`, `FUNCTION_ID`) 
VALUES 
('Work Researcher Detail Cost Report', 'This report lists the individual billable item costs group by the billable item type realated to a researcher', 'ResearcherCostReport.jrxml', 
(select ID from study.ark_module where name='Work Tracking'),
(select ID from study.ark_function where name='BILLABLE_ITEM')
);


-- Study detail cost report
INSERT INTO `reporting`.`report_template` (`NAME`, `DESCRIPTION`, `TEMPLATE_PATH`, `MODULE_ID`, `FUNCTION_ID`) 
VALUES 
('Work Study Detail Cost Report', 'This report lists the individual billable item costs group by the billable item type related to context study', 'StudyDetailCostReport.jrxml', 
(select ID from study.ark_module where name='Work Tracking'),
(select ID from study.ark_function where name='BILLABLE_ITEM')
);

-- Researcher detail cost report related changes. 
ALTER TABLE `admin`.`bilable_item` ADD COLUMN `GST` DOUBLE NULL  AFTER `TOTAL_GST` ;

ALTER TABLE `admin`.`bilable_item` ADD COLUMN `GST_ALLOW` VARCHAR(45) NULL  AFTER `GST` ;

ALTER TABLE `admin`.`bilable_item` CHANGE COLUMN `QUANTITY` `QUANTITY` DOUBLE NULL DEFAULT NULL  ;

ALTER TABLE `admin`.`billable_item_type` ADD COLUMN `QUANTITY_TYPE` VARCHAR(45) NULL  AFTER `TYPE` ;

ALTER TABLE `admin`.`billable_item_type` DROP COLUMN `GST` ;

ALTER TABLE `admin`.`bilable_item` DROP FOREIGN KEY `fk_bilable_item_status` ;

drop table `admin`.`billable_item_status`;

drop table `admin`.`link_billable_item_subject`;






