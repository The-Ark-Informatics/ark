---------------------------------------------
-- [1] Update LOV Descriptions - Anton Kattan
---------------------------------------------
    --  Referring Doctor
    select setval('ix_listofvalues_desc_seq', (select max ("DESCKEY")+1 from ix_listofvalues_description));
    INSERT INTO ix_listofvalues_description ("TYPE", "DESCRIPTION")VALUES('REF_DOCTOR', 'Referring Doctor');

    --  Consent Version
    select setval('ix_listofvalues_desc_seq', (select max ("DESCKEY")+1 from ix_listofvalues_description));
    INSERT INTO ix_listofvalues_description ("TYPE", "DESCRIPTION")VALUES('CONSENTVERSION', 'Consent Version');

    --  Event Type
    select setval('ix_listofvalues_desc_seq', (select max ("DESCKEY")+1 from ix_listofvalues_description));
    INSERT INTO ix_listofvalues_description ("TYPE", "DESCRIPTION")VALUES('EVENTTYPE', 'Event Type');

--------------------------------------------
-- [2]
--------------------------------------------
