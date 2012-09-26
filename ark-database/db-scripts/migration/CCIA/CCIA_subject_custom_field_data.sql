-- insert custom field data: alert
-- assumes the custom field has been created
-- NOTE: the custom field should be setup as a CHARACTER type

-- "MEDICARENO"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'MEDICARENO'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.MEDICARENO text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.MEDICARENO IS NOT NULL;

-- "HOSPITALUR"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'HOSPITALUR'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.HOSPITALUR text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.HOSPITALUR IS NOT NULL;

-- "HOSPITALNAME"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'HOSPITALNAME'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.HOSPITALNAME text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.HOSPITALNAME IS NOT NULL;

-- "OTHERID"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'OTHERID'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.OTHERID text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.OTHERID IS NOT NULL;

-- "DATA_VERFD"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'OTHERID'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.OTHERID text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.OTHERID IS NOT NULL;

-- "PATIENTCOMMENTS"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'PATIENTCOMMENTS'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.PATIENTCOMMENTS text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.PATIENTCOMMENTS IS NOT NULL;

-- "CLINICALSTUDIES"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'CLINICALSTUDIES'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.CLINICALSTUDIES text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.CLINICALSTUDIES IS NOT NULL;

-- "ALERT"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'ALERT'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.ALERT text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.ALERT IS NOT NULL;

-- "INITIALS"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'INITIALS'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.INITIALS text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.INITIALS IS NOT NULL;

-- "STUDYNO"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'STUDYNO'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.STUDYNO text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.STUDYNO IS NOT NULL;

-- "PROTOCOL"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'PROTOCOL'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.PROTOCOL text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.PROTOCOL IS NOT NULL;

-- "CLINICALSTATUS"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'CLINICALSTATUS'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.CLINICALSTATUS text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.CLINICALSTATUS IS NOT NULL;

-- "LFU"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'LFU'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.LFU text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.LFU IS NOT NULL;

-- "EFS"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'EFS'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.EFS text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.EFS IS NOT NULL;

-- "STUDYPRO"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'STUDYPRO'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.STUDYPRO text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.STUDYPRO IS NOT NULL;

-- "CENTRENO"
INSERT study.subject_custom_field_data(link_subject_study_id, custom_field_display_id, text_data_value) 
SELECT l.id link_subject_study_id, (SELECT cfd.id as custom_field_display_id
FROM
	`study`.`custom_field_display` `cfd`,
	`study`.`custom_field` `cf`,
	`study`.`study` `s`
WHERE cf.id = cfd.custom_field_id
AND cf.name = 'CENTRENO'
AND s.id = cf.study_id
AND s.name = 'CCIA') custom_field_display_id, p.CENTRENO text_data_value
FROM study.link_subject_study l 
INNER JOIN ix_patient p
ON l.subject_uid = p.patientid
WHERE p.CENTRENO IS NOT NULL;