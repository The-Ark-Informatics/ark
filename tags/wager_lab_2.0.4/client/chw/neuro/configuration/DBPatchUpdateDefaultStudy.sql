-- Update all biospecimen entries that do not have a default study key
update ix_biospecimen set "STUDYKEY" = '1' where "STUDYKEY" IS NULL or "STUDYKEY" = '-1';