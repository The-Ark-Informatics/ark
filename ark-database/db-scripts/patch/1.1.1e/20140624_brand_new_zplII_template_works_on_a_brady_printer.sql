

-- Trav's made a biospecimen printer label using zebra languge that is tested working on brady bbp33.  Hopefully works everywhere.
INSERT INTO `lims`.`barcode_label` (`NAME`, `DESCRIPTION`, `LABEL_PREFIX`, `LABEL_SUFFIX`, `VERSION`) 
VALUES ('zebra or brady biospecimen', 'Generic biospecimen label using ZPL - tested on brady BBP33', '', '', '1');

-- SET @MAX_LABEL_ID=0;
-- @MAX_LABEL_ID=(SELECT max(id) FROM `lims`.`barcode_label`); 
UPDATE `lims`.`barcode_label` SET `LABEL_PREFIX`='^XA\\n', `LABEL_SUFFIX`='^XZ\\n' WHERE `ID`=26;

select * from lims.barcode_label;

select * from lims.barcode_label where id = 4;


INSERT INTO lims.`barcode_label` (`STUDY_ID`,`BARCODE_PRINTER_ID`,`NAME`,`DESCRIPTION`,`LABEL_PREFIX`,`LABEL_SUFFIX`,`VERSION`,`BARCODE_PRINTER_NAME`) 
VALUES (NULL,1,'zebra biospecimen v3','Generic Zebra Biospecimen Label v3','','',3,NULL);
-- created 26

-- SET @MAX_NEXT_LABEL_ID=0;
-- @MAX_NEXT_LABEL_ID=(SELECT max(id) FROM `lims`.`barcode_label`); 

select * from lims.barcode_label_data where barcode_label_id = 26;

INSERT INTO lims.`barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) 
							VALUES (26,				'^FO',		-10,		20,'^BXN','10','100','\n',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\n');

INSERT INTO lims.`barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) 
							VALUES (26,						'^FD',		'',		'',		NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','^FS\r\n');

INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`)
							VALUES (26,				'b',		105,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n');
INSERT INTO `barcode_label_data` (`ID`, `BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`)
							VALUES (26,				'A',		250,5,'0','1','1','1','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\r\n');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`)
							VALUES (26,				'A',		250,35,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\r\n');


select * from lims.barcode_label_data where BARCODE_LABEL_ID = 4;


USE `lims`;

INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) 
						  VALUES (9,					'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) 
						  VALUES (9,					'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'\"','${firstLineOfCircle}','\"','\n');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) 
						  VALUES (9,					'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'\"','${secondLineOfCircle}','\"','\n');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) 
						  VALUES (9,					'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'\"','${lastLineOfCircle}','\"','\n');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) 
						  VALUES (9,					'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'\"','${biospecimenUid}','\"','\n');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) 
						  VALUES (9,					'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'\"','${dateOfBirth}','\"','\n');
