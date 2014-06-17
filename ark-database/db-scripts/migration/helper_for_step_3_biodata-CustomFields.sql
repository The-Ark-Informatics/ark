

SELECT * FROM wagerlab.ix_biodata_field;


SELECT * FROM wagerlab.ix_biodata_field 
	where fieldkey in(
		SELECT fieldkey FROM wagerlab.ix_biodata_field_group
			where groupkey in (
				select groupkey from wagerlab.ix_biodata_group
					where group_name = 'WAFSS BioData Details'
								)
						);

select * from wagerlab.ix_biodata_group;
'25.000000000000000000000000000000', 'WAFSS BioData Details', 'BIOSPECIMEN'

select count(*) from lims.biospecimen where study_id = 17

select * from lims.biospecimen_custom_field_data where study_id = 17

SELECT count(*)
FROM wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_BIOSPECIMEN bio,study.custom_field cf, study.custom_field_display cfd,
lims.biospecimen bs
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME = @STUDY_GROUP_NAME -- like (@STUDYNAME || '%')
AND bg.DOMAIN = 'BIOSPECIMEN'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = bio.BIOSPECIMENKEY
AND bio.DELETED = 0
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND STRING_VALUE IS NOT NULL
AND bf.LOVTYPE IS NOT NULL
AND bs.OLD_ID = bio.BIOSPECIMENKEY;


SELECT * FROM lims.biospecimen_custom_field_data
where biospecimen_id in (select id from lims.biospecimen where study_id = 17);


select count(*) from lims.biospecimen_custom_field_data
where id > 100593 ;  -- target number is 72285

select * from lims.biospecimen where id in (
	select biospecimen_id from lims.biospecimen_custom_field_data where id > 100590  and id < 100600
	);

select * from study.study where id in (415, 421, 409);

-- d elete from  lims.biospecimen_custom_field_data
-- where  id > 100593 ;  


SELECT * FROM wagerlab.ix_biodata_group;

SET @STUDY_GROUP_NAME = 'WAFSS BioData Details';  -- however there could be more than one of these perhaps?
SET @STUDYKEY = 17;
SET @STUDYNAME= 'WAFSS';


SELECT bs.id AS BIOSPECIMEN_ID, cfd.id AS CUSTOM_FIELD_DISPLAY_ID, SUBSTRING_INDEX(TRIM(TRAILING  SUBSTRING(cf.encoded_values, INSTR(cf.ENCODED_VALUES, concat('=', bd.STRING_VALUE, ';'))) FROM cf.ENCODED_VALUES), ';', -1) AS TEXT_DATA_VALUE, NULL AS `DATE_DATA_VALUE`, NULL AS`NUMBER_DATA_VALUE`, NULL AS `ERROR_DATA_VALUE`
FROM 	wagerlab.IX_BIODATA bd, wagerlab.IX_BIODATA_FIELD bf, wagerlab.IX_BIODATA_TYPES bft, wagerlab.IX_BIODATA_FIELD_GROUP bfg, 
		wagerlab.IX_BIODATA_GROUP bg, wagerlab.IX_BIOSPECIMEN bio,study.custom_field cf, study.custom_field_display cfd,
lims.biospecimen bs
WHERE bfg.GROUPKEY = bg.GROUPKEY
AND bfg.FIELDKEY = bf.FIELDKEY
AND bf.DOMAIN = bg.DOMAIN
AND bf.TYPEKEY = bft.TYPEKEY
AND bg.GROUP_NAME = @STUDY_GROUP_NAME -- like (@STUDYNAME || '%')
AND bg.DOMAIN = 'BIOSPECIMEN'
AND bd.FIELDKEY = bf.FIELDKEY
AND bd.DOMAINKEY = bio.BIOSPECIMENKEY
AND bio.DELETED = 0
AND bio.studykey =  @STUDYKEY
AND cf.study_id = @STUDYKEY
AND ark_function_id = (SELECT ID FROM study.ark_function WHERE name = 'BIOSPECIMEN')
AND cf.id = cfd.custom_field_id
AND cf.NAME = bf.COLUMNNAME
AND STRING_VALUE IS NOT NULL
AND bf.LOVTYPE IS NOT NULL
AND bs.OLD_ID = bio.BIOSPECIMENKEY; 
