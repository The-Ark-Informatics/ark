-- Add a new "comments" column to the [subject_file] table
ALTER TABLE `subject_file` ADD COLUMN `COMMENTS`  text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL AFTER `USER_ID`;
