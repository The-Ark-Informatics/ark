--Bugzilla Bug 495: Search value for "Species" needs to be blank ---------------------------------------------------------------
--first line received from bugzilla but the value existed but was deleted.
--insert into ix_listofvalues ("TYPE", "VALUE", "DESCRIPTION", "SORTORDER", "LANGUAGE") values ('SPECIES', '', '', 1, 'ENG');
update ix_listofvalues set "DELETED" = 0 where "TYPE"='SPECIES' and  "DELETED" = -1;
--END Bugzilla Bug 495: Search value for "Species" needs to be blank ---------------------------------------------------------------
