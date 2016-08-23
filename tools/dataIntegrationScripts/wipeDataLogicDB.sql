    select
        distinct biocollect1_.BIOCOLLECTION_UID as col_0_0_ 
    from
        lims.biospecimen biospecime0_,
        lims.biocollection biocollect1_ 
    where
        biospecime0_.BIOCOLLECTION_ID=biocollect1_.ID 
        and (
            biocollect1_.BIOCOLLECTION_UID not in (
              -- 'AC', '13414fw3q453q24', 'AD', 'AA', 'AB', '1234567890', 
				'eweweewewe'
-- , 'AABBCCDDFF', '999999999999999', 'AABBCCDDFFDD', '8888888888'
            )
        ) 
        and (
            biospecime0_.id not in  (
                328050, 328127, 328128, 328131, 328143, 328136, 328138, 328139, 328142, 328051
            )
        ) 
        and biospecime0_.STUDY_ID=86
        and biocollect1_.STUDY_ID=86;

    select
        distinct biocollect1_.BIOCOLLECTION_UID as col_0_0_ 
    from
        lims.biospecimen biospecime0_,
        lims.biocollection biocollect1_ 
    where
        biospecime0_.BIOCOLLECTION_ID=biocollect1_.ID 
        and (
            biospecime0_.id in (
                328050, 328127, 328128, 328131, 328143, 328136, 328138, 328139, 328142, 328051
            )
        ) 
        and biospecime0_.STUDY_ID=86

select distinct lss.id from au.org.theark.core.model.study.entity.LinkSubjectStudy lss  where lss.study.id = 86null and lss.person.firstName like '%gg%'

select * -- distinct biocollection_id
 from lims.biospecimen where id in (328050, 328127, 328128, 328131, 328143, 328136, 328138, 328139, 328142, 328051);

select * from biocollection where id in (16499,16500);

select * from biocollection where BIOCOLLECTION_UID in (
              'AC', '13414fw3q453q24', 'AD', 'AA', 'AB', '1234567890', 'eweweewewe', 'AABBCCDDFF', '999999999999999', 'AABBCCDDFFDD', '8888888888'
            );
