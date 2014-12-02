ALTER TABLE `lims`.`biospecimen_custom_field_data` 
DROP INDEX `FK_BIOSPECFDATA_BIOSPECIMEN_ID` 
, ADD UNIQUE INDEX `FK_BIOSPECFDATA_BIOSPECIMENID_CFDID` (`BIOSPECIMEN_ID` ASC, `CUSTOM_FIELD_DISPLAY_ID` ASC) ;

select * from `lims`.`biospecimen_custom_field_data`  a,  `lims`.`biospecimen_custom_field_data`  b
where a.biospecimen_id = b.biospecimen_id
and a.id<>b.ID
and a.CUSTOM_FIELD_DISPLAY_ID = b.CUSTOM_FIELD_DISPLAY_ID