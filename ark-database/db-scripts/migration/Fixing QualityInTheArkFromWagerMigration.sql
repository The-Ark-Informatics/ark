select distinct quality 
from wagerlab.ix_biospecimen
where quality is not null
-- NULL
/*'Fresh'
'Unknown'
'Frozen long term (>6mths)'
'N/A'
'Frozen long term (<6mths)'
'Frozen short term (<6mths)'
*/

select * from wagerlab.ix_biospecimen where quality = 'Frozen long term (<6mths)'

select * from lims.biospecimen_quality
/*
'1','Fresh'
'2','Frozen short term (<6mths)'
'3','Frozen long term (>6mths)'
*/

-- so add the following
INSERT INTO lims.`biospecimen_quality` (`ID`,`NAME`) VALUES (-1,'Unknown');
INSERT INTO lims.`biospecimen_quality` (`ID`,`NAME`) VALUES (4,'Frozen long term (<6mths)'); -- this makes no sense...but exists in wager so bringing over
INSERT INTO lims.`biospecimen_quality` (`ID`,`NAME`) VALUES (5,'N/A');

select * from lims.biospecimen_quality;
/*
'-1','Unknown'
'1','Fresh'
'2','Frozen short term (<6mths)'
'3','Frozen long term (>6mths)'
'4','Frozen long term (<6mths)'
'5','N/A'

I can confirm they match now.  So I can fix erroneous current data, and then make sure the data is migrated properly in the future
*/
select * from wagerlab.ix_biospecimen ixb, lims.biospecimen lb
where ixb.biospecimenid = lb.biospecimen_uid
and ixb.studykey = lb.study_id
and ixb.quality is not null and lb.biospecimen_quality_id is null;

-- number of rows matched...i am fairly confident we can put this in after we backup our 
UPDATE  lims.biospecimen lb
JOIN
    wagerlab.ix_biospecimen ixb ON lb.biospecimen_uid = ixb.biospecimenid
        AND lb.study_id = ixb.studykey 
SET 
    lb.biospecimen_quality_id = (select id from lims.biospecimen_quality where name=ixb.quality)
WHERE
    lb.biospecimen_quality_id IS NULL AND ixb.quality IS NOT NULL;


