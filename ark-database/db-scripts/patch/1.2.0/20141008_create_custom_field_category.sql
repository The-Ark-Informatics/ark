/****
* Creating the capability for a custom field to belong to a category.
*  So far it has been requested the field belongs to one category and one custom field only
*****/

CREATE TABLE study.`custom_field_category` (
	`ID` int(11) NOT NULL AUTO_INCREMENT,
	`PARENT_ID` int(11) NULL,
	`STUDY_ID` int(11) NULL,
	`NAME` varchar(255) NOT NULL,
	`DESCRIPTION` text NULL,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB charset=latin1;


