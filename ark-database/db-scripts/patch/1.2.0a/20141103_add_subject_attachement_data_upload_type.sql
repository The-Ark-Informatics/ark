set @id = 0;

set @module_id = 0;

SELECT @id := max(id)+1 from `study`.`upload_type`;

SELECT @module_id := id from `study`.`ark_module` where name='Subject';

INSERT INTO `study`.`upload_type` (`ID`, `NAME`, `DESCRIPTION`, `ARK_MODULE_ID`) VALUES (@id, 'Subject Attachment Data', 'Upload the subject attachments and consent attachments', @module_id);

