use study;

set @id = 0;

set @moduleId = 0;

SELECT @id := max(id)+1 from upload_type;

SELECT @moduleId := id from ark_module where name = 'Subject';

INSERT INTO upload_type (`ID`, `NAME`, `DESCRIPTION`, `ARK_MODULE_ID`)
VALUES (@id, 'Pedigree Data', 'Pedigree Data associated with the subjects', @moduleId);

