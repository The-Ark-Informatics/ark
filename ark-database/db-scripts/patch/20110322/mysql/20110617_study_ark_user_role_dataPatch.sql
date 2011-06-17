/* Set up a Super Administrator. Make user an Ark User already exists */
/* A Super administrator has access to all modules */
INSERT INTO `study`.`ark_user_role` (`ID`, `ARK_USER_ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`) VALUES (1, 1, 1, 1);
INSERT INTO `study`.`ark_user_role` (`ID`, `ARK_USER_ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`) VALUES (2, 1, 1, 2);
INSERT INTO `study`.`ark_user_role` (`ID`, `ARK_USER_ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`) VALUES (3, 1, 1, 3);
INSERT INTO `study`.`ark_user_role` (`ID`, `ARK_USER_ID`, `ARK_ROLE_ID`, `ARK_MODULE_ID`) VALUES (4, 1, 1, 4);
