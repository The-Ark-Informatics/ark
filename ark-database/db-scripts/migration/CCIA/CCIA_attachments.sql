INSERT INTO `study`.`subject_file`
(`LINK_SUBJECT_STUDY_ID`,
`STUDY_COMP_ID`,
`FILENAME`,
`PAYLOAD`,
`USER_ID`,
`COMMENTS`)
SELECT lss.id LINK_SUBJECT_STUDY_ID, NULL as STUDY_COMP_ID, at.file_name, at.file_blob as payload, ixa.attachedby as user_id, concat_ws(' ', ixa.comments) as comments
FROM `ccia-live-28july`.ix_patient p, `ccia-live-28july`.ix_attachments ixa, 
`study`.attachments at, `study`.link_subject_study lss
WHERE p.patientkey = ixa.id
AND ixa.domain = 'Patient' 
AND ixa.deleted =0
AND ixa.file_name IS NOT NULL 
AND ixa.file_name = at.file_name
AND p.patientid = lss.subject_uid
AND lss.study_id = 68;

INSERT INTO `study`.`subject_file`
(`LINK_SUBJECT_STUDY_ID`,
`STUDY_COMP_ID`,
`FILENAME`,
`PAYLOAD`,
`USER_ID`,
`COMMENTS`)
SELECT lss.id LINK_SUBJECT_STUDY_ID, 29 as STUDY_COMP_ID, at.file_name, at.file_blob as payload, ixa.attachedby as user_id, concat_ws(' ', ixa.comments) as comments
FROM `ccia-live-28july`.ix_patient p, `ccia-live-28july`.ix_biospecimen b, `ccia-live-28july`.ix_attachments ixa, 
`study`.attachments at, `study`.link_subject_study lss
WHERE p.patientkey = b.patientkey
AND b.biospecimenkey = ixa.id
AND ixa.domain = 'Biospecimen' 
AND ixa.deleted = 0
AND ixa.file_name IS NOT NULL 
AND ixa.file_name = at.file_name
AND p.patientid = lss.subject_uid
AND lss.study_id = 68;