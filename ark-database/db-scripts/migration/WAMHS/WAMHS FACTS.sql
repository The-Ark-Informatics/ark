select * from zeus.study;


select * from zeus.subject where studykey = 11; -- 11.000000000000000000000000000000, MH-00113712G, CRAIG, ROBERT, LUBCKE, 1966-10-18 00:00:00, 70 Hillman Rd, DARKAN, , 6392, , 97361131, panoramafarm@bigpond.com, 113712.000000000000000000000000000000, 2009-02-24 13:46:45, 2014-02-06 16:35:32, , , Mr, , M, , , , , 1.000000000000000000000000000000, , , 091553B, , , 

select * 

select distinct admissionkey from wagerlab.ix_biospecimen where studykey = 11 and deleted = 0 order by admissionkey;

select distinct admissionkey from wagerlab.ix_admissions where studykey = 11 and deleted = 0 order by admissionkey;

select * from wagerlab.ix_biospecimen where admissionkey like '24782%' 
order by admissionkey; -- encounter MH03132, biospecimenid 08MH03132A, patient 64703, biospecimenkey 1728287
-- but this admission doesnt exist
select * from wagerlab.ix_admissions where admissionkey like '24782%' order by admissionkey -- this does

select * from wagerlab.ix_biospecimen where admissionkey like '24782%' order by admissionkey

select * from wagerlab.ix_biospecimen where admissionkey like '3873%' order by admissionkey

select * from wagerlab.ix_admissions
where admissionid in (select encounter from  -- select * from 
wagerlab.ix_biospecimen where studykey = 11 
and deleted = 0 and admissionkey is null); 

select * from wagerlab.ix_admissions where  studykey=11


select * from wagerlab.ix_biospecimen 
where  studykey=11 and encounter is null and deleted = 0 
and admissionkey is null


select distinct patientkey from wagerlab.ix_biospecimen 
where  studykey=11 and encounter is null and deleted = 0 
and admissionkey is null;


select * from wagerlab.ix_biospecimen 
where  studykey=11 and encounter is null and deleted = 0 
and admissionkey is null;

update blah
set admission_key = xxx
where studykey=11 and encounter is null and deleted = 0 and admissionkey is null;




/* '55303.000000000000000000000000000000'
'55277.000000000000000000000000000000'
'56730.000000000000000000000000000000'
'56757.000000000000000000000000000000'
'55052.000000000000000000000000000000'
'55055.000000000000000000000000000000'
'55202.000000000000000000000000000000'*/

select * from wagerlab.ix_admissions where patientkey in (55303,
55277,
56730,
56757,
55052,
55055,
55202 )
-- only this exists...create fake biospecimen for all of the above - '0.000000000000000000000000000000', '23-NOV-10 08.27.09.864570 AM +08:00', NULL, 'MH04218', '55202.000000000000000000000000000000', 'Nedlands (E Block, SCGH)', NULL, '85604.000000000000000000000000000000', NULL, NULL, '2010-11-23 00:00:00', '56.000000000000000000000000000000', NULL, NULL, NULL, '239.000000000000000000000000000000', NULL, NULL, NULL, NULL, NULL, NULL, '11.000000000000000000000000000000'
)



-- 11.000000000000000000000000000000, MH-00113712G, CRAIG, ROBERT, LUBCKE, 1966-10-18 00:00:00, 70 Hillman Rd, DARKAN, , 6392, , 97361131, panoramafarm@bigpond.com, 113712.000000000000000000000000000000, 2009-02-24 13:46:45, 2014-02-06 16:35:32, , , Mr, , M, , , , , 1.000000000000000000000000000000, , , 091553B, , , 
-- enounter MH01960
-- biospecimen 07MH01960B

select * from zeus.subject where studykey = 11; -- 11.000000000000000000000000000000, MH-00113712G, CRAIG, ROBERT, LUBCKE, 1966-10-18 00:00:00, 70 Hillman Rd, DARKAN, , 6392, , 97361131, panoramafarm@bigpond.com, 113712.000000000000000000000000000000, 2009-02-24 13:46:45, 2014-02-06 16:35:32, , , Mr, , M, , , , , 1.000000000000000000000000000000, , , 091553B, , , 


select * from wagerlab.ix_biospecimen where BIOSPECIMENKEY=1698903

select * from wagerlab.ix_admissions where patientkey = 55228