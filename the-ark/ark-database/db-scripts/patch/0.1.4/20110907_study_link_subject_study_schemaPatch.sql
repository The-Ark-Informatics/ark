use study;
ALTER TABLE `study`.`link_subject_study` DROP FOREIGN KEY `link_subject_study_ibfk_8` , DROP FOREIGN KEY `link_subject_study_ibfk_6` ;
ALTER TABLE `study`.`link_subject_study`	DROP COLUMN `STATE_ID` , 
                                        	DROP COLUMN `COUNTRY_ID` ,
						DROP COLUMN `SITE_POST` , 
						DROP COLUMN `SITE_CITY` , 
						DROP COLUMN `SITE_STREET_ADDRESS` , 
						DROP COLUMN `TOTAL_MAMOGRAMS` , 
						DROP COLUMN `YEAR_OF_RECENT_MAMOGRAM` , 
						DROP COLUMN `YEAR_OF_FIRST_MAMOGRAM` , 
						DROP COLUMN `AMDRF_ID` , 
						DROP INDEX `STATE_ID` , DROP INDEX `COUNTRY_ID` ;


