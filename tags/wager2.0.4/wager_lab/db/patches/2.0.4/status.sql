alter table ix_biospecimen add status varchar2(20);
update  ix_listofvalues set value = 'New' where type='STATUS' and value='Active';
update  ix_listofvalues set value = 'Archived' where type='STATUS' and value='Inactive';
delete from ix_listofvalues where type='STATUS' and value='Deleted';