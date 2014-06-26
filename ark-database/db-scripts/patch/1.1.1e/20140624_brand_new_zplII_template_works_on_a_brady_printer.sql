

-- Trav's made a biospecimen printer label using zebra languge that is tested working on brady bbp33.  Hopefully works everywhere.
INSERT INTO `lims`.`barcode_label` (`NAME`, `DESCRIPTION`, `LABEL_PREFIX`, `LABEL_SUFFIX`, `VERSION`) 
VALUES ('zebra or brady biospecimen', 'Generic biospecimen label using ZPL - tested on brady BBP33', '', '', '1');

select * from lims.barcode_label;


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
