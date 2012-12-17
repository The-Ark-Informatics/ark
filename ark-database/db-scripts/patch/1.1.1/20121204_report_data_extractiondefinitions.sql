INSERT INTO `reporting`.`demographic_field` (`ENTITY`, `FIELD_NAME`, `PUBLIC_FIELD_NAME`, `FIELD_TYPE_ID`) 
VALUES ('Person', 'lastName', 'Last Name', '1'),
        ('Person', 'genderType', 'Sex', '1'),
        ('Person', 'vitalStatus', 'Vital Status', '1'),
        ('Person', 'titleType', 'Title', '1'),
        ('Person', 'maritalStatus', 'Marital Status', '1'),
        ('Person', 'dateOfBirth', 'DOB', '1'),
        ('Person', 'dateOfDeath', 'Date of Death', '1'),
        ('Person', 'causeOfDeath', 'Cause of Death', '1'),
        ('Person', 'preferredEmail', 'Preferred Email', '1'),
        ('Person', 'otherEmail', 'Other Email', '1'),
        ('Person', 'dateLastKnownAlive', 'Last Known Alive', '1');

INSERT INTO `reporting`.`demographic_field` (`ENTITY`, `FIELD_NAME`, `PUBLIC_FIELD_NAME`, `FIELD_TYPE_ID`) 
VALUES 
        ('Address', 'addressLine1', 'Address', '1'),
        ('Address', 'streetAddress', 'Street Address', '1'),
        ('Address', 'city', 'City', '1'),
        ('Address', 'country', 'Country', '1'),
        ('Address', 'state', 'State', '1'),
        ('Address', 'otherState', 'Other State', '1'),
        ('Address', 'addressStatus', 'Address Status', '1'),
        ('Address', 'addressType', 'Address Type', '1');

INSERT INTO `reporting`.`demographic_field` (`ENTITY`, `FIELD_NAME`, `PUBLIC_FIELD_NAME`, `FIELD_TYPE_ID`) 
VALUES 
        ('Phone', 'phoneNumber', 'Phone Number', '1');

INSERT INTO `reporting`.`biospecimen_field` (`ENTITY`,`FIELD_NAME`,`PUBLIC_FIELD_NAME`,`FIELD_TYPE_ID`)
VALUES
        ('Biospecimen','biospecimenUid','BiospecimenUID','1'),
        ('Biospecimen','sampleType','Sample Type','1'),
        ('Biospecimen','sampleDate','Sample Date','3'),
        ('Biospecimen','sampleTime','Sample Time','3'),
        ('Biospecimen','processedDate','Processed Date','3'),
        ('Biospecimen','processedTime','Processed Time','3'),
        ('Biospecimen','quantity','Quantity','1'),
        ('Biospecimen','concentration','Concentration','1'),
        ('Biospecimen','purity','Purity','1');

INSERT INTO `reporting`.`biocollection_field` (`ENTITY`,`FIELD_NAME`,`PUBLIC_FIELD_NAME`,`FIELD_TYPE_ID`)
VALUES
        ('BioCollection','biocollectionUid','Biocollection UID','1'),
        ('BioCollection','comments','Comments','1');

/* more biocollection and others to come...but for now... here is a start */
