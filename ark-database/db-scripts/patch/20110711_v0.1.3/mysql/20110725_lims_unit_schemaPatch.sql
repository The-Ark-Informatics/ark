USE lims;

CREATE TABLE `unit` (
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(45) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

Insert into `unit` (ID,NAME) values (1,'mm');
Insert into `unit` (ID,NAME) values (2,'ug/L');
Insert into `unit` (ID,NAME) values (3,'Years');
Insert into `unit` (ID,NAME) values (4,'mµ/l');
Insert into `unit` (ID,NAME) values (5,'bpm');
Insert into `unit` (ID,NAME) values (6,'g/L');
Insert into `unit` (ID,NAME) values (7,'fL');
Insert into `unit` (ID,NAME) values (8,'feet');
Insert into `unit` (ID,NAME) values (9,'IU/L');
Insert into `unit` (ID,NAME) values (10,'kg');
Insert into `unit` (ID,NAME) values (11,'U');
Insert into `unit` (ID,NAME) values (12,'µV');
Insert into `unit` (ID,NAME) values (13,'Days');
Insert into `unit` (ID,NAME) values (14,'mg/l');
Insert into `unit` (ID,NAME) values (15,'Age');
Insert into `unit` (ID,NAME) values (16,'cm');
Insert into `unit` (ID,NAME) values (17,'m/L');
Insert into `unit` (ID,NAME) values (18,'Iµ/mL');
Insert into `unit` (ID,NAME) values (19,'pg');
Insert into `unit` (ID,NAME) values (20,'row 2');
Insert into `unit` (ID,NAME) values (21,'grams');
Insert into `unit` (ID,NAME) values (22,'pred');
Insert into `unit` (ID,NAME) values (23,'Gy');
Insert into `unit` (ID,NAME) values (24,'Hours');
Insert into `unit` (ID,NAME) values (25,'µ/L');
Insert into `unit` (ID,NAME) values (26,'Mins');
Insert into `unit` (ID,NAME) values (27,'%');
Insert into `unit` (ID,NAME) values (28,'mS');
Insert into `unit` (ID,NAME) values (29,'mm/hr');
Insert into `unit` (ID,NAME) values (30,'mg/dl');
Insert into `unit` (ID,NAME) values (31,'mn');
Insert into `unit` (ID,NAME) values (33,'mg/L');
Insert into `unit` (ID,NAME) values (34,'kgm2');
Insert into `unit` (ID,NAME) values (35,'mm Hg');
Insert into `unit` (ID,NAME) values (36,'kg/m2');
Insert into `unit` (ID,NAME) values (37,'Pipes');
Insert into `unit` (ID,NAME) values (38,'L');
Insert into `unit` (ID,NAME) values (39,'S');
Insert into `unit` (ID,NAME) values (40,'m');
Insert into `unit` (ID,NAME) values (41,'fl');
Insert into `unit` (ID,NAME) values (42,'hours');
Insert into `unit` (ID,NAME) values (43,'mm/hg');
