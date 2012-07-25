USE study;
ALTER TABLE `study`.`correspondences` DROP FOREIGN KEY `correspondences_status_type_id` ;
ALTER TABLE `study`.`correspondences` DROP COLUMN `STATUS_TYPE_ID`, DROP INDEX `status_type` ;
DROP TABLE `study`.`correspondence_status_type`;