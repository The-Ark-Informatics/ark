USE `study`;

SET @module_id = (select id from `ark_module` where `NAME` = "Calendar");

SET @admin = (select id from `ark_role` where name = "Calendar Administrator");
SET @man = (select id from `ark_role` where name = "Calendar Data Manager");
SET @ro = (select id from `ark_role` where name = "Calendar Read-Only User");

SET @function_id = (select id from `ark_function` where name = "CALENDAR");
INSERT INTO `ark_role_policy_template` (`ark_role_id`, `ark_module_id`, `ark_function_id`, `ark_permission_id`) VALUES
(@admin, @module_id, @function_id, 1),
(@admin, @module_id, @function_id, 2),
(@admin, @module_id, @function_id, 3),
(@admin, @module_id, @function_id, 4),
(@man, @module_id, @function_id, 1),
(@man, @module_id, @function_id, 2),
(@man, @module_id, @function_id, 3),
(@ro, @module_id, @function_id, 2);
