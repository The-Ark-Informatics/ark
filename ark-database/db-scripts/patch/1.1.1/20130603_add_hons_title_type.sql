use study;

set @id = 0;

SELECT @id := max(id)+1 from title_type;

INSERT INTO title_type (`ID`,`NAME`, `DESCRIPTION`) 
VALUES (@id,'Hons', 'Hons');
