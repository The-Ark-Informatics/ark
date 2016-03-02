set @config_id = (select id from `config`.config_fields where name="ROWS_PER_PAGE");
set @default_value = (select default_value from `config`.config_fields where id = @config_id);

insert into `config`.user_config (`USER_ID`, `FIELD_ID`, `VALUE`) select id, @config_id, @default_value from `study`.ark_user where id not in (select user_id from `config`.user_config where field_id = @config_id);

set @config_id = (select id from `config`.config_fields where name="CUSTOM_FIELDS_PER_PAGE");
set @default_value = (select default_value from `config`.config_fields where id = @config_id);

insert into `config`.user_config (`USER_ID`, `FIELD_ID`, `VALUE`) select id, @config_id, @default_value from `study`.ark_user where id not in (select user_id from `config`.user_config where field_id = @config_id);