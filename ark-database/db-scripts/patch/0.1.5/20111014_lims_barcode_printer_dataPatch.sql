USE lims;

-- printers
-- NOTE: This is dummy reference data to mimic when a study is setup, hence for now study = NULL
INSERT INTO `barcode_printer` (`STUDY_ID`,`NAME`,`DESCRIPTION`,`LOCATION`,`HOST`,`PORT`) VALUES (NULL,'zebra','Zebra TLP 2844','ARK Office','130.95.56.99','9100');
INSERT INTO `barcode_printer` (`STUDY_ID`,`NAME`,`DESCRIPTION`,`LOCATION`,`HOST`,`PORT`) VALUES (NULL,'brady_bbp_11','Brady BPP 11 34L','Paul''s Office','130.95.56.43','9100');

-- labels
-- NOTE: This is dummy reference data to mimic when a study is setup, hence for now study = NULL
INSERT INTO `barcode_label` (`STUDY_ID`,`BARCODE_PRINTER_ID`,`NAME`,`DESCRIPTION`,`LABEL_PREFIX`,`LABEL_SUFFIX`) VALUES (NULL,1,'zebra biospecimen','General Zebra Biospecimen Label','D14%0Aq457%0AN%0A','P1%0A');
INSERT INTO `barcode_label` (`STUDY_ID`,`BARCODE_PRINTER_ID`,`NAME`,`DESCRIPTION`,`LABEL_PREFIX`,`LABEL_SUFFIX`) VALUES (NULL,2,'brady biospecimen','Generic Brady Biospecimen label','J%0A','A%201%0A');
INSERT INTO `barcode_label` (`STUDY_ID`,`BARCODE_PRINTER_ID`,`NAME`,`DESCRIPTION`,`LABEL_PREFIX`,`LABEL_SUFFIX`) VALUES (NULL,1,'zebra bioCollection','Generic BioCollection label','D15%0AN%0A','P1%0A');
INSERT INTO `barcode_label` (`STUDY_ID`,`BARCODE_PRINTER_ID`,`NAME`,`DESCRIPTION`,`LABEL_PREFIX`,`LABEL_SUFFIX`) VALUES (NULL,2,'brady straw barcode','Generic Brady Straw Biospecimen label','DIRECTION 0%0AREFERENCE 0,0%0ACLS%0A','PRINT 1,1%0A');

-- label data templates
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (1,'b',200,15,'D','h3',NULL,NULL,NULL,NULL,NULL,NULL,'%22','${biospecimenUid}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (1,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'%22','${firstLineOfCircle}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (1,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'%22','${secondLineOfCircle}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (1,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'%22','${lastLineOfCircle}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (1,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'%22','${biospecimenUid}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (1,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'%22','${dateOfBirth}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (3,'A',240,10,'1','2','1','1','N',NULL,NULL,NULL,'%22','ID: {$subjectUid} Family ID: ${familyId}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (3,'A',220,10,'1','2','1','1','N',NULL,NULL,NULL,'%22','ASRB No: ${asrbno}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (3,'A',200,10,'1','2','1','1','N',NULL,NULL,NULL,'%22','Collection Date: ${collectionDate}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (3,'A',180,10,'1','2','1','1','N',NULL,NULL,NULL,'%22','Researcher: ${refDoctor}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (3,'A',160,10,'1','2','1','1','N',NULL,NULL,NULL,'%22','DOB: ${dateOfBirth} Sex: {$sex}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (4,'BARCODE',25,120,'%2239%22','96','1','0','2','4',NULL,NULL,'%22','${biospecimenUid}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (4,'TEXT',25,145,'%223%22','0','1','1',NULL,NULL,NULL,NULL,'%22','${biospecimenUid}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (4,'TEXT',25,160,'%223%22','0','1','1',NULL,NULL,NULL,NULL,'%22','${dateOfBirth}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (4,'BARCODE',250,120,'%2239%22','96','1','0','2','4',NULL,NULL,'%22','${biospecimenUid}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (4,'TEXT',250,145,'%223%22','0','1','1',NULL,NULL,NULL,NULL,'%22','${biospecimenUid}','%22','%0A');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (4,'TEXT',250,160,'%223%22','0','1','1',NULL,NULL,NULL,NULL,'%22','${dateOfBirth}','%22','%0A');