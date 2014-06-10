/***** research - discovered subject file is being used for storing consent files...we just need to clearly indicate that a file is consent based with some binary flag

select * from study.consent;

select * from study.consent_file;

select * from study.subject_file;

desc study.consent_file;

desc  study.subject_file;

insert into study.consent_file(consent_id, filename, payload, checksum, user_id, insert_time)
select 193668, filename, payload, checksum, user_id, current_time from study.subject_file where id = 994
*****************/

ALTER TABLE `study`.`subject_file` 
	ADD COLUMN `IS_CONSENT_FILE` TINYINT NOT NULL DEFAULT 0  AFTER `COMMENTS` ;


