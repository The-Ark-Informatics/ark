

select * from wagerlab.ix_biospecimen where studykey in (2);

select * from wagerlab.ix_biospecimen where studykey in (3);

select * from wagerlab.ix_biospecimen where biospecimenid like '%Q%3%';

select * from wagerlab.ix_biospecimen where ENCOUNTER like '%Q%3%';

select * from zeus.subject where studykey in (2); -- 1667 subject returned, with minmal informations from around early 2007

select * from zeus.subject where studykey in (3); -- 1667 subject returned, with minmal informations from around early 2007


