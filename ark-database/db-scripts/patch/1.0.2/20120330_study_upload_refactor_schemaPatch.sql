alter table study.upload
add column status varchar(64)

add constraint status in 'UPLOADING', 'COMPLETE', 'ERRORS'

 

create table upload_error(
id pk,
error_msg,
row_number,
original_row_data
)

#add constraint error_msg in 'UPLOADING', 'COMPLETE', 'ERRORS'


