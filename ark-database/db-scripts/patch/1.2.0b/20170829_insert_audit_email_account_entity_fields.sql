INSERT INTO `audit`.`audit_entity` (`CLASS_IDENTIFIER`, `NAME`, `PACKAGE_ID`) VALUES ('au.org.theark.core.model.study.entity.EmailAccount', 'Email Account', (SELECT ID FROM `audit`.`audit_package` WHERE NAME = 'Study'));


INSERT INTO `audit`.`audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`) VALUES ('name', 'Name', (SELECT ID FROM `audit`.`audit_entity` WHERE CLASS_IDENTIFIER = 'au.org.theark.core.model.study.entity.EmailAccount' ));
INSERT INTO `audit`.`audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`) VALUES ('emailAccountType', 'Email Account Type', (SELECT ID FROM `audit`.`audit_entity` WHERE CLASS_IDENTIFIER = 'au.org.theark.core.model.study.entity.EmailAccount' ));
INSERT INTO `audit`.`audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`) VALUES ('emailStatus', 'Email Status', (SELECT ID FROM `audit`.`audit_entity` WHERE CLASS_IDENTIFIER = 'au.org.theark.core.model.study.entity.EmailAccount' ));
INSERT INTO `audit`.`audit_field` (`FIELD_NAME`, `NAME`, `ENTITY_ID`) VALUES ('primaryAccount', 'Primary Account', (SELECT ID FROM `audit`.`audit_entity` WHERE CLASS_IDENTIFIER = 'au.org.theark.core.model.study.entity.EmailAccount' ));


