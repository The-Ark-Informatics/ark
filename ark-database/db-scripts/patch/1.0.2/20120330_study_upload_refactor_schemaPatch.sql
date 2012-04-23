alter table study.upload
add column STATUSstudy.upload ENUM('UPLOADING', 'COMPLETE', 'ERRORS');



create table upload_error(
id int primary key,
upload_id int,	
error_msg varchar(256),
row_number int,
original_row_data text,
foreign key fk_upload_error_upload 
	(upload_id) references study.upload(id)
)


