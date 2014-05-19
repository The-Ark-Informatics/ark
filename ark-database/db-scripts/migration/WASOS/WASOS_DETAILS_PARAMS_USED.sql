SET @STUDYKEY = 414;
SET @STUDYNAME= 'WASOS';
SET @AUTOGEN_SUBJECT = 1;
SET @AUTOGEN_BIOSPECIMEN = 1;
SET @AUTOGEN_BIOCOLLECTION = 1;
-- before setting each of these params check that this can work...ie; that there is not some weird multiple prefix for a given study.

-- SET @SUBJECT_PADCHAR = 8; -- no of chars to pad out
-- apparently subject prefix comes from wager
-- SET @SUBJECT_PREFIX = 'RAV';


SET @BIOCOLLECTIONUID_PREFIX = 'WAS';
-- SET @BIOCOLLECTIONUID_TOKEN_ID = 1;
SET @BIOCOLLECTIONUID_TOKEN_DASH = '';
SET @BIOCOLLECTIONUID_PADCHAR_ID = 5;

SET @BIOSPECIMENUID_PREFIX = 'WAS';
-- SET @BIOSPECIMENUID_TOKEN_ID = 1;
SET @BIOSPECIMENUID_PADCHAR_ID = 6;

-- SET @SITE_PERMITTED = 'WADB (SCGH)' ;  -- IF MORE THAN ONE FIX THIS 
