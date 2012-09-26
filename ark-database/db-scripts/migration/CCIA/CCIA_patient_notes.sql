INSERT INTO `study`.`subject_file`
(`LINK_SUBJECT_STUDY_ID`,
`STUDY_COMP_ID`,
`FILENAME`,
`PAYLOAD`,
`COMMENTS`)
SELECT lss.id LINK_SUBJECT_STUDY_ID, sc.id STUDY_COMP_ID, n.filename, pn.file_blob as payload, concat_ws('', n.date, n.name, ' ', n.description) as comments
FROM `ccia-live-28july`.ix_patient p, `ccia-live-28july`.ix_notes n, `study`.patient_notes pn, `study`.study_comp sc, `study`.link_subject_study lss
WHERE n.filename = pn.file_name
AND n.filename IS NOT NULL 
AND p.patientkey = n.elementkey
AND sc.name = n.type
AND p.patientid = lss.subject_uid
AND lss.study_id = 68
ORDER BY n.filename;