USE lims;

-- printers
-- NOTE: ASSUMES A STUDY_ID EXISTS!!!
INSERT INTO `barcode_printer` (`ID`,`STUDY_ID`,`NAME`,`DESCRIPTION`,`LOCATION`,`HOST`,`PORT`) VALUES (1,1,'zebra','Zebra TLP 2844','ARK Office','130.95.56.99',9100);
INSERT INTO `barcode_printer` (`ID`,`STUDY_ID`,`NAME`,`DESCRIPTION`,`LOCATION`,`HOST`,`PORT`) VALUES (2,1,'brady_bpp_11','Brady BPP 11','Paul''s Office','130.95.56.43',9100);

-- label
-- NOTE: ASSUMES A STUDY_ID EXISTS!!!
INSERT INTO `barcode_label` (`ID`,`STUDY_ID`,`BARCODE_PRINTER_ID`,`NAME`,`DESCRIPTION`,`LABEL_PREFIX`,`LABEL_SUFFIX`) VALUES (1,1,1,'zebra biospecimen','General Zebra Biospecimen Label','D14%0Aq457%0AN%0A','P1%0A');
INSERT INTO `barcode_label` (`ID`,`STUDY_ID`,`BARCODE_PRINTER_ID`,`NAME`,`DESCRIPTION`,`LABEL_PREFIX`,`LABEL_SUFFIX`) VALUES (2,1,2,'brady biospecimen','Generic Brady Biospecimen label','J%0A','A%201%0A');
INSERT INTO `barcode_label` (`ID`,`STUDY_ID`,`BARCODE_PRINTER_ID`,`NAME`,`DESCRIPTION`,`LABEL_PREFIX`,`LABEL_SUFFIX`) VALUES (3,1,1,'zebra bioCollection','Generic BioCollection label','D15%0AN%0A','P1%0A');

-- label data templates
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (2,1,'b',200,15,'D','h3',NULL,NULL,NULL,NULL,NULL,NULL,'%22','${biospecimenUid}','%22','%0A');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (3,1,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'%22','${firstLineOfCircle}','%22','%0A');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (4,1,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'%22','${secondLineOfCircle}','%22','%0A');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (5,1,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'%22','${lastLineOfCircle}','%22','%0A');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (6,1,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'%22','${biospecimenUid}','%22','%0A');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (7,1,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'%22','${dateOfBirth}','%22','%0A');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (8,3,'A',240,10,'1','2','1','1','N',NULL,NULL,NULL,'%22','ID: {$subjectUid} Family ID: ${familyId}','%22','%0A');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (9,3,'A',220,10,'1','2','1','1','N',NULL,NULL,NULL,'%22','ASRB No: ${asrbno}','%22','%0A');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (10,3,'A',200,10,'1','2','1','1','N',NULL,NULL,NULL,'%22','Collection Date: ${collectionDate}','%22','%0A');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (11,3,'A',180,10,'1','2','1','1','N',NULL,NULL,NULL,'%22','Researcher: ${refDoctor}','%22','%0A');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (12,3,'A',160,10,'1','2','1','1','N',NULL,NULL,NULL,'%22','DOB: ${dateOfBirth} Sex: {$sex}','%22','%0A');
