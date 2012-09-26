INSERT INTO `study`.`subject_file`
(`LINK_SUBJECT_STUDY_ID`,
`STUDY_COMP_ID`,
`FILENAME`,
`PAYLOAD`,
`USER_ID`,
`COMMENTS`)
SELECT lss.id LINK_SUBJECT_STUDY_ID, 28 as STUDY_COMP_ID, cf.file_name, cf.file_blob as payload, cs.researcher as user_id, concat_ws(' ', cs.date_approved, c.comments, cs.comments) as comments
FROM `ccia-live-28july`.ix_patient p, `ccia-live-28july`.ix_consent c, `ccia-live-28july`.ix_consent_study cs, 
`study`.consent_files cf, `study`.link_subject_study lss
WHERE p.patientkey = c.patientkey
AND c.deleted = 0
AND cs.deleted = 0
AND cs.consentkey = c.consentkey
AND cs.file_name IS NOT NULL 
AND cs.file_name = cf.file_name
AND p.patientid = lss.subject_uid
AND lss.study_id = 68;