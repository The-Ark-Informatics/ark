CREATE OR REPLACE VIEW `field_summary` AS select `STUDY_ID` AS `study_id`,
count(`ID`) AS `fields`,
(select count(distinct `fd`.`FIELD_ID`) from `field_data` `fd`, `field_collection` `fc` where (`fd`.`COLLECTION_ID` = `fc`.`COLLECTION_ID` AND `fc`.`STUDY_ID` = `f`.`STUDY_ID`)) AS `fields_with_data` 
from `field` `f` group by `f`.`STUDY_ID`;
