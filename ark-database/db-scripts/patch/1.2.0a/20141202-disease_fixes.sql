USE `study`;

DELETE FROM `ark_module_function` WHERE ark_function_id in (select id from ark_function where name = "POSITION");

DELETE FROM `ark_function` WHERE name = "POSITION";

SET @module_id = (select id from `ark_module` where `NAME` = "Disease");

INSERT INTO `ark_function` (`NAME`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ("DISEASE_CUSTOM_FIELDS", 1, "tab.module.disease.customfield");
SET @function_id = (select id from `ark_function` where `name` = "DISEASE_CUSTOM_FIELDS");
INSERT INTO `ark_module_function` (`ark_module_id`, `ark_function_id`, `function_sequence`) VALUES (@module_id, @function_id, 1);

INSERT INTO `ark_function` (`NAME`, `ARK_FUNCTION_TYPE_ID`, `RESOURCE_KEY`) VALUES ("DISEASE_AFFECTION", 1, "tab.module.disease.affection");
SET @function_id = (select id from `ark_function` where `name` = "DISEASE_AFFECTION");
INSERT INTO `ark_module_function` (`ark_module_id`, `ark_function_id`, `function_sequence`) VALUES (@module_id, @function_id, 1);

SET @sub_module_id = (select id from `ark_module` where `NAME` = "Subject");
INSERT INTO `ark_module_function` (`ark_module_id`, `ark_function_id`, `function_sequence`) VALUES (@sub_module_id, @function_id, 13);

INSERT INTO `ark_role` (`NAME`, `DESCRIPTION`) VALUES ('Disease Administrator', NULL), ('Disease Data Manager', NULL), ('Disease Read-Only user', NULL);

SET @admin = (select id from `ark_role` where name = "Disease Administrator");
SET @man = (select id from `ark_role` where name = "Disease Data Manager");
SET @ro = (select id from `ark_role` where name = "Disease Read-Only user");

SET @function_id = (select id from `ark_function` where name = "DISEASE");
INSERT INTO `ark_role_policy_template` (`ark_role_id`, `ark_module_id`, `ark_function_id`, `ark_permission_id`) VALUES
(@admin, @module_id, @function_id, 1),
(@admin, @module_id, @function_id, 2),
(@admin, @module_id, @function_id, 3),
(@admin, @module_id, @function_id, 4),
(@man, @module_id, @function_id, 1),
(@man, @module_id, @function_id, 2),
(@man, @module_id, @function_id, 3),
(@ro, @module_id, @function_id, 2);

SET @function_id = (select id from `ark_function` where `name` = "DISEASE_CUSTOM_FIELDS");
INSERT INTO `ark_role_policy_template` (`ark_role_id`, `ark_module_id`, `ark_function_id`, `ark_permission_id`) VALUES
(@admin, @module_id, @function_id, 1),
(@admin, @module_id, @function_id, 2),
(@admin, @module_id, @function_id, 3),
(@admin, @module_id, @function_id, 4),
(@man, @module_id, @function_id, 1),
(@man, @module_id, @function_id, 2),
(@man, @module_id, @function_id, 3),
(@ro, @module_id, @function_id, 2);

SET @function_id = (select id from `ark_function` where `name` = "DISEASE_AFFECTION");
INSERT INTO `ark_role_policy_template` (`ark_role_id`, `ark_module_id`, `ark_function_id`, `ark_permission_id`) VALUES
(@admin, @module_id, @function_id, 1),
(@admin, @module_id, @function_id, 2),
(@admin, @module_id, @function_id, 3),
(@admin, @module_id, @function_id, 4),
(@man, @module_id, @function_id, 1),
(@man, @module_id, @function_id, 2),
(@man, @module_id, @function_id, 3),
(@ro, @module_id, @function_id, 2);

INSERT INTO `ark_module_role` (`ark_module_id`, `ark_role_id`) VALUES (@module_id, @admin);
INSERT INTO `ark_module_role` (`ark_module_id`, `ark_role_id`) VALUES (@module_id, @man);
INSERT INTO `ark_module_role` (`ark_module_id`, `ark_role_id`) VALUES (@module_id, @ro);

USE `disease`;

CREATE TABLE `affection_position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `AFFECTION_ID` int(11) NOT NULL,
  `POSITION_ID` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `affection`
ADD `STUDY_ID` int(11) NOT NULL,
ADD `RECORD_DATE` date NOT NULL
