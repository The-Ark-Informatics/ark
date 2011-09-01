-- ----------------------------
-- Records of report_output_format
-- ----------------------------
INSERT INTO `report_output_format` VALUES ('1', 'PDF', 'Portable Document Format (compatible with Adobe Reader)');
INSERT INTO `report_output_format` VALUES ('2', 'CSV', 'Comma Separated Value (compatible with Excel)');

-- ----------------------------
-- Records of report_template
-- ----------------------------
INSERT INTO `report_template` VALUES ('1', 'Study Summary Report', 'This report provides an overview of subject information for a study.  It contains information like: <ul>   <li>total subject count</li>   <li>subject counts grouped by subject status</li>   <li>subject counts grouped by consent status</li> </ul>', 'StudySummaryReport.jrxml', '1', '23');
INSERT INTO `report_template` VALUES ('2', 'Study-level Consent Details Report', 'This report lists detailed subject information for a particular study based on their consent status at the study-level.', 'ConsentDetailsReport.jrxml', '2', '24');
INSERT INTO `report_template` VALUES ('3', 'Study Component Consent Details Report', 'This report lists detailed subject information for a particular study based on their consent status for a specific study component.', 'ConsentDetailsReport.jrxml', '2', '25');
INSERT INTO `report_template` VALUES ('4', 'Phenotypic Field Details Report (Data Dictionary)', 'This report lists detailed field information for a particular study based on their associated phenotypic collection.', 'DataDictionaryReport.jrxml', '3', '26');