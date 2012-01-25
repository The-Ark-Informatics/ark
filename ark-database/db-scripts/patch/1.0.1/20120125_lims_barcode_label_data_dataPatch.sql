USE lims;
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (42,1,'b',200,15,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (43,1,'A',100,20,'0','2','1','1','N',NULL,NULL,NULL,'"','${firstLineOfCircle}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (44,1,'A',100,40,'0','2','1','1','N',NULL,NULL,NULL,'"','${secondLineOfCircle}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (45,1,'A',115,60,'0','2','1','1','N',NULL,NULL,NULL,'"','${lastLineOfCircle}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (46,1,'A',260,15,'0','1','1','2','N',NULL,NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (47,1,'A',260,45,'0','1','1','2','N',NULL,NULL,NULL,'"','${dateOfBirth}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (48,2,'A',240,10,'1','2','1','1','N',NULL,NULL,NULL,'"','ID: {$subjectUid} Family ID: ${familyId}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (49,2,'A',220,10,'1','2','1','1','N',NULL,NULL,NULL,'"','ASRB No: ${asrbno}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (50,2,'A',200,10,'1','2','1','1','N',NULL,NULL,NULL,'"','Collection Date: ${collectionDate}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (51,2,'A',180,10,'1','2','1','1','N',NULL,NULL,NULL,'"','Researcher: ${refDoctor}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (52,2,'A',160,10,'1','2','1','1','N',NULL,NULL,NULL,'"','DOB: ${dateOfBirth} Sex: {$sex}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (53,3,'BARCODE',25,120,'"39"','96','1','0','2','4',NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (54,3,'TEXT',25,145,'"3"','0','1','1',NULL,NULL,NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (55,3,'TEXT',25,160,'"3"','0','1','1',NULL,NULL,NULL,NULL,'"','${dateOfBirth}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (56,3,'BARCODE',250,120,'"39"','96','1','0','2','4',NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (57,3,'TEXT',250,145,'"3"','0','1','1',NULL,NULL,NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (58,3,'TEXT',250,160,'"3"','0','1','1',NULL,NULL,NULL,NULL,'"','${dateOfBirth}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (93,4,'b',195,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (94,4,'b',105,5,'D','h3','c16','r16',NULL,NULL,NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (95,4,'A',250,5,'0','1','1','1','N',NULL,NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`ID`,`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (96,4,'A',250,35,'0','1','1','2','N',NULL,NULL,NULL,'"','${dateOfBirth}','"','
');
-- Zebra straw
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (5,'B',225,0,'0','1','2','2','75','N',NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (5,'B',-100,0,'0','1','2','2','75','N',NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (5,'A',250,75,'0','1','1','2','N',NULL,NULL,NULL,'"','${biospecimenUid}','"','
');
INSERT INTO `barcode_label_data` (`BARCODE_LABEL_ID`,`COMMAND`,`X_COORD`,`Y_COORD`,`P1`,`P2`,`P3`,`P4`,`P5`,`P6`,`P7`,`P8`,`QUOTE_LEFT`,`DATA`,`QUOTE_RIGHT`,`LINE_FEED`) VALUES (5,'A',-100,75,'0','1','1','2','N',NULL,NULL,NULL,'"','${biospecimenUid}','"','
');
