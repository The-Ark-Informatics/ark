
use study;

set @id = 0;

SELECT @id := max(id)+1 from field_type;

INSERT INTO field_type(id, name) values (@id,'COMPLEX');

