CREATE OR REPLACE VIEW `pheno`.`field_summary` AS 
select `fc`.`STUDY_ID` AS `study_id`,
count(`fc`.`FIELD_ID`) AS `fields`,
(select count(`fd`.`FIELD_ID`) AS `COUNT(fd.field_id)` 
from `pheno`.`field_data` `fd` 
where (`fd`.`COLLECTION_ID` = `fc`.`COLLECTION_ID`)) AS `fields_with_data` 
from `pheno`.`field_collection` `fc`
group by `fc`.`STUDY_ID`;
