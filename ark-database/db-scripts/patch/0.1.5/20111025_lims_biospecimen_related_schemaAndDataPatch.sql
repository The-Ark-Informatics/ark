USE lims;

-- biospecimenuid_padchar
/*
Not Needed, Refer to 20111024_lims_biospecimenuid_schemaPatch.sql patch
CREATE TABLE `biospecimenuid_padchar` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(25) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;


INSERT INTO `biospecimenuid_padchar` (ID,NAME) VALUES (1,'1');
INSERT INTO `biospecimenuid_padchar` (ID,NAME) VALUES (2,'2');
INSERT INTO `biospecimenuid_padchar` (ID,NAME) VALUES (3,'3');
INSERT INTO `biospecimenuid_padchar` (ID,NAME) VALUES (4,'4');
INSERT INTO `biospecimenuid_padchar` (ID,NAME) VALUES (5,'5');
INSERT INTO `biospecimenuid_padchar` (ID,NAME) VALUES (6,'6');
INSERT INTO `biospecimenuid_padchar` (ID,NAME) VALUES (7,'7');
INSERT INTO `biospecimenuid_padchar` (ID,NAME) VALUES (8,'8');
INSERT INTO `biospecimenuid_padchar` (ID,NAME) VALUES (9,'9');
INSERT INTO `biospecimenuid_padchar` (ID,NAME) VALUES (10,'10');


-- biospecimenuid_sequence
CREATE TABLE `biospecimenuid_sequence` (
`STUDY_NAME_ID`  varchar(150) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
`UID_SEQUENCE`  int(11) NOT NULL DEFAULT 0 ,
`INSERT_LOCK`  int(11) NOT NULL DEFAULT 0 ,
PRIMARY KEY (`STUDY_NAME_ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;

-- biospecimenuid_token
CREATE TABLE `biospecimenuid_token` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(25) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;

INSERT INTO `biospecimenuid_token` (ID,NAME) VALUES (1,'-');
INSERT INTO `biospecimenuid_token` (ID,NAME) VALUES (2,'@');
INSERT INTO `biospecimenuid_token` (ID,NAME) VALUES (3,'#');
INSERT INTO `biospecimenuid_token` (ID,NAME) VALUES (4,':');
INSERT INTO `biospecimenuid_token` (ID,NAME) VALUES (5,'*');
INSERT INTO `biospecimenuid_token` (ID,NAME) VALUES (6,'|');
INSERT INTO `biospecimenuid_token` (ID,NAME) VALUES (7,'_');
INSERT INTO `biospecimenuid_token` (ID,NAME) VALUES (8,'+');

-- biospecimen_anticoagulant
CREATE TABLE `biospecimen_anticoagulant` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;

INSERT INTO `biospecimen_anticoagulant` (ID,NAME) VALUES (1,'N/A');
INSERT INTO `biospecimen_anticoagulant` (ID,NAME) VALUES (2,'EDTA');
INSERT INTO `biospecimen_anticoagulant` (ID,NAME) VALUES (3,'Lithium Heparin');
INSERT INTO `biospecimen_anticoagulant` (ID,NAME) VALUES (4,'Sodium Citrate');

-- biospecimen_grade
CREATE TABLE `biospecimen_grade` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;

INSERT INTO `biospecimen_grade` (ID,NAME) VALUES (1,'Extracted');
INSERT INTO `biospecimen_grade` (ID,NAME) VALUES (2,'Precipitated');

-- biospecimen_quality
CREATE TABLE `biospecimen_quality` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;

INSERT INTO `biospecimen_quality` (ID,NAME) VALUES (1,'Fresh');
INSERT INTO `biospecimen_quality` (ID,NAME) VALUES (2,'Frozen short term (<6mths)');
INSERT INTO `biospecimen_quality` (ID,NAME) VALUES (3,'Frozen long term (>6mths)');

-- biospecimen_species
CREATE TABLE `biospecimen_species` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;
/*
NN - Moved this script into 20111020_lims_3_biospecimen_schemaPatch.sql 
INSERT INTO `biospecimen_species` (ID,NAME) VALUES (1,'Human');
INSERT INTO `biospecimen_species` (ID,NAME) VALUES (2,'Baboon');
INSERT INTO `biospecimen_species` (ID,NAME) VALUES (3,'Cat');
INSERT INTO `biospecimen_species` (ID,NAME) VALUES (4,'Cow');
INSERT INTO `biospecimen_species` (ID,NAME) VALUES (5,'Dog');
INSERT INTO `biospecimen_species` (ID,NAME) VALUES (6,'Goat');
INSERT INTO `biospecimen_species` (ID,NAME) VALUES (7,'Mouse');
INSERT INTO `biospecimen_species` (ID,NAME) VALUES (8,'Pig');
INSERT INTO `biospecimen_species` (ID,NAME) VALUES (9,'Rabbit');
INSERT INTO `biospecimen_species` (ID,NAME) VALUES (10,'Rat');
INSERT INTO `biospecimen_species` (ID,NAME) VALUES (11,'Sheep');


-- biospecimen_status
CREATE TABLE `biospecimen_status` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;

INSERT INTO `biospecimen_status` (ID,NAME) VALUES (1,'New');
INSERT INTO `biospecimen_status` (ID,NAME) VALUES (2,'Archived');

-- biospecimen_storage
CREATE TABLE `biospecimen_storage` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
`SIZE`  double NULL DEFAULT NULL ,
`UNIT_ID`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`ID`),
CONSTRAINT `fk_biospecimen_storage_unit` FOREIGN KEY (`UNIT_ID`) REFERENCES `unit` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
INDEX `fk_biospecimen_storage_unit` USING BTREE (`UNIT_ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;

INSERT INTO `biospecimen_storage` (ID,NAME,SIZE,UNIT_ID) VALUES (43,'0.5ml',0.5,17);
INSERT INTO `biospecimen_storage` (ID,NAME,SIZE,UNIT_ID) VALUES (44,'1.5ml',1.5,17);
INSERT INTO `biospecimen_storage` (ID,NAME,SIZE,UNIT_ID) VALUES (45,'10ml tube',10,17);
INSERT INTO `biospecimen_storage` (ID,NAME,SIZE,UNIT_ID) VALUES (46,'2ml tube',2,17);
INSERT INTO `biospecimen_storage` (ID,NAME,SIZE,UNIT_ID) VALUES (47,'2ml',2,17);
INSERT INTO `biospecimen_storage` (ID,NAME,SIZE,UNIT_ID) VALUES (48,'50ml tube',50,17);
INSERT INTO `biospecimen_storage` (ID,NAME,SIZE,UNIT_ID) VALUES (49,'96 well plate',NULL,NULL);
INSERT INTO `biospecimen_storage` (ID,NAME,SIZE,UNIT_ID) VALUES (50,'Large tube',NULL,NULL);
INSERT INTO `biospecimen_storage` (ID,NAME,SIZE,UNIT_ID) VALUES (51,'Parrafin Block',NULL,NULL);


-- biospecimenuid_template
-- refer 20111024_lims_biospecimenuid_template_schemaPatch.sql
CREATE TABLE `biospecimenuid_template` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`STUDY_ID`  int(11) NOT NULL ,
`BIOSPECIMENUID_TOKEN_ID`  int(11) NULL DEFAULT NULL ,
`BIOSPECIMENUID_PREFIX`  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL ,
`BIOSPECIMENUID_PADCHAR_ID`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`ID`),
CONSTRAINT `fk_study_biospecimenuid_padchar` FOREIGN KEY (`BIOSPECIMENUID_PADCHAR_ID`) REFERENCES `biospecimenuid_padchar` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT `fk_study_biospecimenuid_token` FOREIGN KEY (`BIOSPECIMENUID_TOKEN_ID`) REFERENCES `biospecimenuid_token` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT `fk_study_study` FOREIGN KEY (`STUDY_ID`) REFERENCES `study`.`study` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
INDEX `fk_study_study` USING BTREE (`STUDY_ID`),
INDEX `fk_study_biospecimenuid_padchar` USING BTREE (`BIOSPECIMENUID_PADCHAR_ID`),
INDEX `fk_study_biospecimenuid_token` USING BTREE (`BIOSPECIMENUID_TOKEN_ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
ROW_FORMAT=Compact
;
*/

-- biospecimen
-- Applied this irrespective of the ark-test instance having the ritght column and not null constraint as I cannot find where this alter statement was introduced. As part of the create biospeciment prior 0.1.4 release this column was nullable
ALTER TABLE `biospecimen` MODIFY COLUMN `LINK_SUBJECT_STUDY_ID`  int(11) NOT NULL AFTER `STUDY_ID`; 

ALTER TABLE `biospecimen` MODIFY COLUMN `SAMPLETYPE_ID`  int(11) NOT NULL AFTER `LINK_SUBJECT_STUDY_ID`;

ALTER TABLE `biospecimen` MODIFY COLUMN `COLLECTION_ID`  int(11) NOT NULL AFTER `SAMPLETYPE_ID`; -- this was needed to rearrange

-- ALTER TABLE `biospecimen` ADD COLUMN `BIOSPECIMEN_STORAGE_ID`  int(11) NULL DEFAULT NULL AFTER `OTHERID`; -- Not required. This has been added by a script elsewhere.

ALTER TABLE `biospecimen` MODIFY COLUMN `DEPTH`  int(11) NULL DEFAULT 1 ; -- Redundant but does not cause any issue

/*
 * Not needed refer to patch 20111020_lims_3_biospecimen_schemaPatch.sql
ALTER TABLE `biospecimen` ADD COLUMN `BIOSPECIMEN_GRADE_ID`  int(11) NULL DEFAULT NULL AFTER `DEPTH`; -- 

ALTER TABLE `biospecimen` ADD COLUMN `SAMPLE_DATE`  datetime NULL DEFAULT NULL AFTER `BIOSPECIMEN_GRADE_ID`; 
*/

ALTER TABLE `biospecimen` MODIFY COLUMN `SAMPLE_TIME`  time NULL DEFAULT NULL ; -- Redundant but does not cause any issue
/*
 Not needed refer to patch 20111020_lims_3_biospecimen_schemaPatch.sql
ALTER TABLE `biospecimen` ADD COLUMN `PROCESSED_DATE`  datetime NULL DEFAULT NULL AFTER `SAMPLE_TIME`; -- 

ALTER TABLE `biospecimen` ADD COLUMN `PROCESSED_TIME`  time NULL DEFAULT NULL AFTER `PROCESSED_DATE`; -- Not needed refer to patch 20111020_lims_3_biospecimen_schemaPatch.sql
*/

ALTER TABLE `biospecimen` MODIFY COLUMN `SAMPLETYPE`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ;

--ALTER TABLE `biospecimen` ADD COLUMN `BIOSPECIMEN_SPECIES_ID`  int(11) NULL DEFAULT 1 AFTER `SAMPLESUBTYPE`;  -- Not needed refer to patch 20111020_lims_3_biospecimen_schemaPatch.sql

ALTER TABLE `biospecimen` MODIFY COLUMN `QTY_COLLECTED`  double NULL DEFAULT NULL ; -- Redundant but does not cause any issue


ALTER TABLE `biospecimen` MODIFY COLUMN `QTY_REMOVED`  double NULL DEFAULT NULL AFTER `QTY_COLLECTED`; -- Redundant but does not cause any issue

ALTER TABLE `biospecimen` MODIFY COLUMN `COMMENTS`  text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL AFTER `QTY_REMOVED`; -- Redundant but does not cause any issue

ALTER TABLE `biospecimen` MODIFY COLUMN `QUANTITY`  double NULL DEFAULT NULL AFTER `COMMENTS`; -- Redundant but does not cause any issue

ALTER TABLE `biospecimen` MODIFY COLUMN `TREATMENT_TYPE_ID`  int(11) NOT NULL AFTER `UNIT_ID`;

ALTER TABLE `biospecimen` MODIFY COLUMN `BARCODED`  tinyint(1) NOT NULL DEFAULT 0 AFTER `TREATMENT_TYPE_ID`;


/*
 * The below DDL's not required.Refer to patch 20111020_lims_3_biospecimen_schemaPatch.sql
ALTER TABLE `biospecimen` ADD COLUMN `BIOSPECIMEN_QUALITY_ID`  int(11) NULL DEFAULT NULL AFTER `BARCODED`; 
ALTER TABLE `biospecimen` ADD COLUMN `BIOSPECIMEN_ANTICOAGULANT_ID`  int(11) NULL DEFAULT NULL AFTER `BIOSPECIMEN_QUALITY_ID`;
ALTER TABLE `biospecimen` ADD COLUMN `BIOSPECIMEN_STATUS_ID`  int(11) NULL DEFAULT NULL AFTER `BIOSPECIMEN_ANTICOAGULANT_ID`;
ALTER TABLE `biospecimen` DROP COLUMN `STORED_IN`;
ALTER TABLE `biospecimen` DROP COLUMN `GRADE`;
ALTER TABLE `biospecimen` DROP COLUMN `SAMPLEDATE`;
ALTER TABLE `biospecimen` DROP COLUMN `EXTRACTED_TIME`;

ALTER TABLE `biospecimen` DROP COLUMN `LOCATION`;
ALTER TABLE `biospecimen` DROP COLUMN `SUBTYPEDESC`;
ALTER TABLE `biospecimen` DROP COLUMN `SPECIES`;

ALTER TABLE `biospecimen` DROP COLUMN `DATEEXTRACTED`;
ALTER TABLE `biospecimen` DROP COLUMN `GESTAT`;
ALTER TABLE `biospecimen` DROP COLUMN `DATEDISTRIBUTED`;

ALTER TABLE `biospecimen` DROP COLUMN `COLLABORATOR`;
ALTER TABLE `biospecimen` DROP COLUMN `DNACONC`;

ALTER TABLE `biospecimen` DROP COLUMN `PURITY`;

ALTER TABLE `biospecimen` DROP COLUMN `ANTICOAG`;

ALTER TABLE `biospecimen` DROP COLUMN `PROTOCOL`;

ALTER TABLE `biospecimen` DROP COLUMN `DNA_BANK`;
ALTER TABLE `biospecimen` DROP COLUMN `QUALITY`;

ALTER TABLE `biospecimen` DROP COLUMN `WITHDRAWN`;
ALTER TABLE `biospecimen` DROP COLUMN `STATUS`;

ALTER TABLE `biospecimen` ADD CONSTRAINT `fk_biospecimen_anticoagulant` FOREIGN KEY (`BIOSPECIMEN_ANTICOAGULANT_ID`) REFERENCES `biospecimen_anticoagulant` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `biospecimen` ADD CONSTRAINT `fk_biospecimen_quality` FOREIGN KEY (`BIOSPECIMEN_QUALITY_ID`) REFERENCES `biospecimen_quality` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
 ALTER TABLE `biospecimen` ADD CONSTRAINT `fk_biospecimen_species` FOREIGN KEY (`BIOSPECIMEN_SPECIES_ID`) REFERENCES `biospecimen_species` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `biospecimen` ADD CONSTRAINT `fk_biospecimen_status` FOREIGN KEY (`BIOSPECIMEN_STATUS_ID`) REFERENCES `biospecimen_status` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `biospecimen` ADD CONSTRAINT `fk_biospecimen_storage` FOREIGN KEY (`BIOSPECIMEN_STORAGE_ID`) REFERENCES `biospecimen_storage` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION;
*/

CREATE INDEX `fk_biospecimen_storage` USING BTREE ON `biospecimen`(`BIOSPECIMEN_STORAGE_ID`);
CREATE INDEX `fk_biospecimen_quality` USING BTREE ON `biospecimen`(`BIOSPECIMEN_QUALITY_ID`);
CREATE INDEX `fk_biospecimen_anticoagulant` USING BTREE ON `biospecimen`(`BIOSPECIMEN_ANTICOAGULANT_ID`);
CREATE INDEX `fk_biospecimen_species` USING BTREE ON `biospecimen`(`BIOSPECIMEN_SPECIES_ID`);

--CREATE INDEX `fk_biospecimen_status` USING BTREE ON `biospecimen`(`BIOSPECIMEN_STATUS_ID`); When executing this, we get Error Code: 1061 Duplicate key name 'fk_biospecimen_status'. Not sure how the other indexes were executed.

-- barcode_label Redundant yet not issues if you ran it
ALTER TABLE `barcode_label` MODIFY COLUMN `LABEL_PREFIX`  text CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL AFTER `DESCRIPTION`;
ALTER TABLE `barcode_label` MODIFY COLUMN `LABEL_SUFFIX`  text CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL AFTER `LABEL_PREFIX`;

-- barcode_label_data
ALTER TABLE `barcode_label_data` MODIFY COLUMN `QUOTE_LEFT`  varchar(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `P8`;
ALTER TABLE `barcode_label_data` MODIFY COLUMN `QUOTE_RIGHT`  varchar(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `DATA`;
ALTER TABLE `barcode_label_data` MODIFY COLUMN `LINE_FEED`  varchar(5) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER `QUOTE_RIGHT`;


