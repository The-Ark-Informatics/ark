USE study;

set @role_id :=0;
set @module_id :=0;
set @function_id :=0;
set @permission_create :=0;
set @permission_read :=0;
set @permission_update :=0;
set @permission_delete :=0;

select @role_id:= id from ark_role where name = 'LIMS Administrator';

select @module_id:= id from ark_module where name = 'LIMS';

select @function_id:= id from ark_function where name = 'BIOSPECIMEN_AND_BIOCOLLECTION_CUSTOM_FIELD_UPLOAD';

select @permission_create:=id from ark_permission where name ='CREATE';

select @permission_read:=id from ark_permission where name ='READ';

select @permission_update:=id from ark_permission where name ='UPDATE';

select @permission_delete:=id from ark_permission where name ='DELETE'; 


insert into ark_role_policy_template (ark_role_id, ark_module_id, ark_function_id, ark_permission_id)
values(@role_id, @module_id, @function_id, @permission_create);
insert into ark_role_policy_template (ark_role_id, ark_module_id, ark_function_id, ark_permission_id)
values(@role_id, @module_id, @function_id, @permission_read);
insert into ark_role_policy_template (ark_role_id, ark_module_id, ark_function_id, ark_permission_id)
values(@role_id, @module_id, @function_id, @permission_update);
insert into ark_role_policy_template (ark_role_id, ark_module_id, ark_function_id, ark_permission_id)
values(@role_id, @module_id, @function_id, @permission_delete);
