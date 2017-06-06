/**
Please copy the 20170522_user_role_description_update_role-descriptions.csv file to server /var/lib/mysql-files/ directory which is the "secure_file_priv" key in sql. 
**/
use study;

CREATE TEMPORARY TABLE temp_update_table LIKE ark_role;
LOAD DATA INFILE '/var/lib/mysql-files/20170522_user_role_description_update_role-descriptions.csv'
INTO TABLE temp_update_table FIELDS TERMINATED BY ',' (id, name, description); 

UPDATE ark_role
INNER JOIN temp_update_table on temp_update_table.id = ark_role.id
SET ark_role.name = temp_update_table.name,
    ark_role.description =temp_update_table.description;

DROP TEMPORARY TABLE temp_update_table;

SET @ID1=(SELECT ID FROM `study`.`ark_role` WHERE name='Geno Read-Only User'); 
SET @ID2=(SELECT ID FROM `study`.`ark_role` WHERE name='New Role'); 

DELETE FROM `study`.`ark_role_policy_template` WHERE `ARK_ROLE_ID`=@ID1;
DELETE FROM `study`.`ark_role` WHERE `ID`=@ID1;
DELETE FROM `study`.`ark_role_policy_template` WHERE `ARK_ROLE_ID`=@ID2;
DELETE FROM `study`.`ark_role` WHERE `ID`=@ID2;

