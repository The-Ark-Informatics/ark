USE `audit`;

SET @REV = 1;

INSERT INTO `aud_link_study_studysite` (`ID`, `REV`, `REVTYPE`, `STUDY_ID`, `STUDY_SITE_ID`)
select `ID`, @REV, 0, `STUDY_ID`, `STUDY_SITE_ID` from `study`.`link_study_studysite`;

INSERT INTO `aud_link_study_substudy` (`ID`, `REV`, `REVTYPE`, `STUDY_ID`, `SUB_STUDY_ID`)
select `ID`, @REV, 0, `STUDY_ID`, `SUB_STUDY_ID` from `study`.`link_study_substudy`;

INSERT INTO `aud_lss_pipeline` (`ID`, `REV`, `REVTYPE`, `LSS_ID`, `PIPELINE_ID`)
select `ID`, @REV, 0, `LSS_ID`, `PIPELINE_ID` from `geno`.`lss_pipeline`;

INSERT INTO `aud_pipeline` (`ID`, `REV`, `REVTYPE`, `DESCRIPTION`, `NAME`, `STUDY_ID`)
select `ID`, @REV, 0, `DESCRIPTION`, `NAME`, `STUDY_ID` from `geno`.`pipeline`;

INSERT INTO `aud_process` (`ID`, `REV`, `REVTYPE`, `DESCRIPTION`, `END_TIME`, `NAME`, `START_TIME`, `COMMAND_ID`, `PIPELINE_ID`)
select `ID`, @REV, 0, `DESCRIPTION`, `END_TIME`, `NAME`, `START_TIME`, `COMMAND_ID`, `PIPELINE_ID` from `geno`.`process`;

INSERT INTO `aud_process_input` (`ID`, `REV`, `REVTYPE`, `INPUT_FILE_HASH`, `INPUT_FILE_LOCATION`, `INPUT_FILE_TYPE`, `INPUT_KEPT`, `INPUT_SERVER`, `PROCESS_ID`)
select `ID`, @REV, 0, `INPUT_FILE_HASH`, `INPUT_FILE_LOCATION`, `INPUT_FILE_TYPE`, `INPUT_KEPT`, `INPUT_SERVER`, `PROCESS_ID` from `geno`.`process_input`;

INSERT INTO `aud_process_output` (`ID`, `REV`, `REVTYPE`, `OUTPUT_FILE_HASH`, `OUTPUT_FILE_LOCATION`, `OUTPUT_FILE_TYPE`, `OUTPUT_KEPT`, `OUTPUT_SERVER`, `PROCESS_ID`)
select `ID`, @REV, 0, `OUTPUT_FILE_HASH`, `OUTPUT_FILE_LOCATION`, `OUTPUT_FILE_TYPE`, `OUTPUT_KEPT`, `OUTPUT_SERVER`, `PROCESS_ID` from `geno`.`process_output`;

INSERT INTO `aud_study_pedigree_config` (`ID`, `REV`, `REVTYPE`, `AGE_ALLOWED`, `DOB_ALLOWED`, `STATUS_ALLOWED`, `CUSTOM_FIELD_ID`, `STUDY_ID`)
select `ID`, @REV, 0, `AGE_ALLOWED`, `DOB_ALLOWED`, `STATUS_ALLOWED`, `CUSTOM_FIELD_ID`, `STUDY_ID` from `study`.`study_pedigree_config`;
