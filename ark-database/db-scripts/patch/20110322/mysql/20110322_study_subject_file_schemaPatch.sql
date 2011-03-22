-- Add a new "comments" column to the [study].[subject_file] table
USE study;
ALTER TABLE `subject_file` ADD COLUMN `COMMENTS`  text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL AFTER `USER_ID`;
