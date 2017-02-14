INSERT INTO config.settings (`type`, `highest_type`, `propertyType`, `property_name`, `property_value`)
SELECT DISTINCT 'system', 'user', `TYPE`, `NAME`, `DEFAULT_VALUE` from config.config_fields;

INSERT INTO config.settings (`type`, `highest_type`, `propertyType`, `property_name`, `property_value`, `user_id`)
SELECT DISTINCT 'user', 'user', config_fields.type, config_fields.name, user_config.value, user_config.user_id from user_config inner join config_fields on user_config.field_id = config_fields.id;
