SELECT * FROM study.subjectuid_sequence;

INSERT INTO `study`.`subjectuid_sequence` (`STUDY_NAME_ID`, `UID_SEQUENCE`, `INSERT_LOCK`) VALUES ('Inherited Retinal Disease', '4927', '0');

SELECT * FROM lims.biospecimenuid_sequence;

DELETE FROM `lims`.`biospecimenuid_sequence` WHERE `STUDY_NAME_ID`='Inherited Retinal Disease';

INSERT INTO `lims`.`biospecimenuid_sequence` (`STUDY_NAME_ID`, `UID_SEQUENCE`, `INSERT_LOCK`) VALUES ('Inherited Retinal Disease', '52051', '0');



SELECT * FROM lims.biocollectionuid_sequence;

DELETE FROM `lims`.`biocollectionuid_sequence` WHERE `STUDY_NAME_ID`='PMH: diabetes and obesity';

INSERT INTO `lims`.`biocollectionuid_sequence` (`STUDY_NAME_ID`, `UID_SEQUENCE`, `INSERT_LOCK`) VALUES ('PMH: diabetes and obesity', '5336', '0');




SELECT * FROM study.subjectuid_sequence;

DELETE FROM  `study`.`subjectuid_sequence` where study_name_id = 'PMH: diabetes and obesity' ;

INSERT INTO `study`.`subjectuid_sequence` (`STUDY_NAME_ID`, `UID_SEQUENCE`, `INSERT_LOCK`) VALUES ('PMH: diabetes and obesity', '3731', '0');-- was 3730 but i will manually move that one guy

SELECT * FROM lims.biospecimenuid_sequence;

DELETE FROM `lims`.`biospecimenuid_sequence` WHERE `STUDY_NAME_ID`='PMH: diabetes and obesity';

INSERT INTO `lims`.`biospecimenuid_sequence` (`STUDY_NAME_ID`, `UID_SEQUENCE`, `INSERT_LOCK`) VALUES ('PMH: diabetes and obesity', '50151', '0');



SELECT * FROM lims.biocollectionuid_sequence;

DELETE FROM `lims`.`biocollectionuid_sequence` WHERE `STUDY_NAME_ID`='PMH: diabetes and obesity';

INSERT INTO `lims`.`biocollectionuid_sequence` (`STUDY_NAME_ID`, `UID_SEQUENCE`, `INSERT_LOCK`) VALUES ('PMH: diabetes and obesity', '5048', '0');

