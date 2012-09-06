ALTER TABLE `study`.`address` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`email_account` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`link_site_contact` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`link_study_studycomp` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`link_study_studysite` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`link_study_substudy` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`link_subject_contact` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`link_subject_studycomp` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`person` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`phone` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`study` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`study_comp` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `study`.`study_site` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`collection` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`collection_import` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`decode_mask` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`encoded_data` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`marker` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`marker_group` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`marker_meta_data` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`meta_data` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`meta_data_field` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`subject_marker_meta_data` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`subject_meta_data` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`upload` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`upload_collection` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;

ALTER TABLE `geno`.`upload_marker_group` 
DROP PRIMARY KEY 
, ADD PRIMARY KEY (`ID`) ;


