/****
* Creating the capability for a custom field to belong to a category.
*  So far it has been requested the field belongs to one category and one custom field only
*****/
-- drop table study.custom_field_category;

CREATE TABLE study.`category` (
	`ID` int(11) NOT NULL AUTO_INCREMENT,
	`PARENT_ID` int(11) NULL,
	`STUDY_ID` int(11) NULL,
	`NAME` varchar(100) NOT NULL,
	`DESCRIPTION` text NULL,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB charset=latin1; -- or shall we go utf 8?

/********
* The concept of one to one can't just be added to the custom field because it is one to one
*	PER Study
* Correction - the custom field is per study...so one to one at that level is fine
*
drop table study.custom_field_category
CREATE TABLE study.`custom_field_category` (
	`ID` int(11) NOT NULL AUTO_INCREMENT,
	`CUSTOM_FIELD_ID` int(11) NULL,
	`CATEGORY_ID` int(11) NULL,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB charset=latin1; -- or shall we go utf 8?
******/
    
ALTER TABLE `study`.`custom_field_display` 
ADD COLUMN `CATEGORY_ID` INT(11) NULL AFTER `ALLOW_MULTIPLE_SELECTION`;




