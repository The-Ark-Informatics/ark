USE lims;

CREATE TABLE `lims`.`biospecimen_custom_field_data` (
  `ID` int(11) NOT NULL auto_increment,
  `BIOSPECIMEN_ID` int(11) NOT NULL,
  `CUSTOM_FIELD_DISPLAY_ID` int(11) NOT NULL,
  `TEXT_DATA_VALUE` text,
  `DATE_DATA_VALUE` datetime default NULL,
  `NUMBER_DATA_VALUE` double default NULL,
  `ERROR_DATA_VALUE` text,
  PRIMARY KEY  (`ID`),
  KEY `FK_BIOSPECFDATA_CUSTOM_FIELD_DISPLAY_ID` (`CUSTOM_FIELD_DISPLAY_ID`),
  CONSTRAINT `FK_BIOSPECFDATA_CUSTOM_FIELD_DISPLAY_ID` FOREIGN KEY (`CUSTOM_FIELD_DISPLAY_ID`) REFERENCES `study`.`custom_field_display` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Ensures that if the biospecimen is deleted, then the associated biospecimenCustomFieldData is also deleted.
ALTER TABLE `lims`.`biospecimen_custom_field_data` 
  ADD CONSTRAINT `FK_BIOSPECFDATA_BIOSPECIMEN_ID`
  FOREIGN KEY (`BIOSPECIMEN_ID` )
  REFERENCES `lims`.`biospecimen` (`ID` )
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

