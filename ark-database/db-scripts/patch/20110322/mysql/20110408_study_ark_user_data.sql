use study;
/* Modules */
INSERT INTO `study`.`ark_module` (`ID`, `NAME`) VALUES (1, 'Study');
INSERT INTO `study`.`ark_module` (`ID`, `NAME`) VALUES (2, 'Subject');
INSERT INTO `study`.`ark_module` (`ID`, `NAME`) VALUES (3, 'Phenotypic');
INSERT INTO `study`.`ark_module` (`ID`, `NAME`) VALUES (4, 'Genotypic');
INSERT INTO `study`.`ark_module` (`ID`, `NAME`) VALUES (5, 'LIMS')

/* Roles */
INSERT INTO `study`.`ark_role` (`ID`, `NAME`) VALUES (1, 'Super Administrator');
INSERT INTO `study`.`ark_role` (`ID`, `NAME`) VALUES (2, 'Administrator');
INSERT INTO `study`.`ark_role` (`ID`, `NAME`) VALUES (3, 'Ordinary User');

/*Permissions */
INSERT INTO `study`.`ark_permission` (`ID`, `NAME`) VALUES (1, 'CREATED');
INSERT INTO `study`.`ark_permission` (`ID`, `NAME`) VALUES (2, 'READ');
INSERT INTO `study`.`ark_permission` (`ID`, `NAME`) VALUES (3, 'UPDATE');
INSERT INTO `study`.`ark_permission` (`ID`, `NAME`) VALUES (4, 'DELETE');

/* Set up Permissions for Roles SA,Admin and Ordinary*/
INSERT INTO `study`.`ark_role_permission` (`ID`, `ARK_ROLE_ID`, `ARK_PERMISSION_ID`) VALUES (1, 1, 1);
INSERT INTO `study`.`ark_role_permission` (`ID`, `ARK_ROLE_ID`, `ARK_PERMISSION_ID`) VALUES (2, 1, 2);
INSERT INTO `study`.`ark_role_permission` (`ID`, `ARK_ROLE_ID`, `ARK_PERMISSION_ID`) VALUES (3, 1, 3);
INSERT INTO `study`.`ark_role_permission` (`ID`, `ARK_ROLE_ID`, `ARK_PERMISSION_ID`) VALUES (4, 1, 4);
INSERT INTO `study`.`ark_role_permission` (`ID`, `ARK_ROLE_ID`, `ARK_PERMISSION_ID`) VALUES (5, 2, 1);
INSERT INTO `study`.`ark_role_permission` (`ID`, `ARK_ROLE_ID`, `ARK_PERMISSION_ID`) VALUES (6, 2, 2);
INSERT INTO `study`.`ark_role_permission` (`ID`, `ARK_ROLE_ID`, `ARK_PERMISSION_ID`) VALUES (7, 2, 3);
INSERT INTO `study`.`ark_role_permission` (`ID`, `ARK_ROLE_ID`, `ARK_PERMISSION_ID`) VALUES (8, 3, 2);
INSERT INTO `study`.`ark_role_permission` (`ID`, `ARK_ROLE_ID`, `ARK_PERMISSION_ID`) VALUES (9, 3, 3);


