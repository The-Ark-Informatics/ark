use study;

-- Set Pheno Data Manager permissions
UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`=2 WHERE `ID`='71';
UPDATE `study`.`ark_role_policy_template` SET `ARK_PERMISSION_ID`=2 WHERE `ID`='73';
DELETE FROM `study`.`ark_role_policy_template` WHERE `ID`='72';
DELETE FROM `study`.`ark_role_policy_template` WHERE `ID`='105';
INSERT INTO `study`.`ark_role_policy_template` (`ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`, `ARK_FUNCTION_ID`, `ARK_PERMISSION_ID`) VALUES (141, 8, 3, 15, 3);