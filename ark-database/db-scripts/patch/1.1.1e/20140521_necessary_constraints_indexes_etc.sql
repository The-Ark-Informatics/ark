ALTER TABLE `lims`.`study_inv_site` 
ADD UNIQUE INDEX `uq_study_inv_site` (`STUDY_ID` ASC, `INV_SITE_ID` ASC) ;
