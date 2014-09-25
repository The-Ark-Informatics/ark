ALTER TABLE study.correspondences DROP FOREIGN KEY correspondences_person_id ;
	
ALTER TABLE study.correspondences
CHANGE COLUMN PERSON_ID PERSON_ID INT(11) NULL ,
	ADD CONSTRAINT correspondence_person_id FOREIGN KEY (PERSON_ID)
	REFERENCES study.person (ID)
	ON DELETE NO ACTION 
	ON UPDATE NO ACTION;
