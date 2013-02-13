select * from `reporting`.`demographic_field`;

/*run 14:40
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
        ('BioCollection','comments','Comments','1'); */

/* run 14:42
INSERT INTO study.field_type(id, name) values (4,'COMPLEX');
*/


/* run 14:44
DELETE FROM study.ark_module_function 
where ark_module_id = (SELECT id from study.ark_module where name='subject')
        AND ark_function_id = (select id from study.ark_function where name='SUBJECT_UPLOAD');

SET @SEQ_ID = 1;

SELECT @SEQ_ID := max(amf.function_sequence)+1 
FROM study.ark_module_function amf
     	inner join study.ark_module am on am.id=amf.ark_module_id   
where am.name='study';

INSERT INTO `study`.`ark_module_function` (`ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `FUNCTION_SEQUENCE`) 
VALUES ((SELECT id from study.ark_module where name='study'), 
        (select id from study.ark_function where name='SUBJECT_UPLOAD'), 
        @SEQ_ID );
*/

/* Run 14:44
use study;

set @id = 0;

SELECT @id := max(id)+1 from title_type;

INSERT INTO title_type (`ID`,`NAME`, `DESCRIPTION`) 
VALUES (@id,'Sen', 'Senator');
*/

/* run 14:45 
delete from study.ark_module_function
where ark_module_id = (Select id from study.ark_module where name='lims')
        and ark_function_id in (select id from study.ark_function where name in ('LIMS_SUBJECT','BIOSPECIMEN'));
*/
