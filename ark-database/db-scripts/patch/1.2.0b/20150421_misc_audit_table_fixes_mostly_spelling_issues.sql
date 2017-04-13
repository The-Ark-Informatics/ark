USE 'audit';

UPDATE audit_field SET FIELD_NAME="invBox" WHERE id=141;
UPDATE audit_field SET FIELD_NAME="dateReceived" WHERE id=203;
UPDATE audit_field SET FIELD_NAME="receivedDate" WHERE id=221;
UPDATE audit_field SET FIELD_NAME="attachmentPayload" WHERE id=235;
UPDATE audit_field SET FIELD_NAME="allowMultiselect" WHERE id=258;
UPDATE audit_field SET FIELD_NAME="dateReceived" WHERE id=320;
UPDATE audit_field SET FIELD_NAME="parentStudy" WHERE id=344;

UPDATE audit_field SET NAME="Unit Type (In Text)" WHERE id=253;

DELETE FROM audit_field WHERE id in (145,147,148,151,152,155,156,158,159,161,163);

-- ALTER TABLE aud_biospecimen ADD PARENT_ID bigint;
-- ALTER TABLE aud_address ADD PERSON_ID bigint;

update audit.aud_address inner join study.address  on  study.address.id = audit.aud_address.id
set audit.aud_address.person_id=study.address.PERSON_ID;
