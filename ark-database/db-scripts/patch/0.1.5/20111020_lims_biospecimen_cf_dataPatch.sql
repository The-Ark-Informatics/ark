USE LIMS;
-- anticoag
INSERT INTO lims.biospecimen_anticoagulant (name) VALUES ('N/A');
INSERT INTO lims.biospecimen_anticoagulant (name) VALUES ('EDTA');
INSERT INTO lims.biospecimen_anticoagulant (name) VALUES ('Lithium Heparin');
INSERT INTO lims.biospecimen_anticoagulant (name) VALUES ('Sodium Citrate');

-- grade
INSERT INTO lims.biospecimen_grade (name) VALUES ('Extracted');
INSERT INTO lims.biospecimen_grade (name) VALUES ('Precipitated');

-- quality
INSERT INTO lims.biospecimen_quality (name) VALUES ('Fresh');
INSERT INTO lims.biospecimen_quality (name) VALUES ('Frozen short term (<6mths)');
INSERT INTO lims.biospecimen_quality (name) VALUES ('Frozen long term (>6mths)');

-- species
INSERT INTO lims.biospecimen_species (name) VALUES ('Human');
INSERT INTO lims.biospecimen_species (name) VALUES ('Baboon');
INSERT INTO lims.biospecimen_species (name) VALUES ('Cat');
INSERT INTO lims.biospecimen_species (name) VALUES ('Cow');
INSERT INTO lims.biospecimen_species (name) VALUES ('Dog');
INSERT INTO lims.biospecimen_species (name) VALUES ('Goat');
INSERT INTO lims.biospecimen_species (name) VALUES ('Mouse');
INSERT INTO lims.biospecimen_species (name) VALUES ('Pig');
INSERT INTO lims.biospecimen_species (name) VALUES ('Rabbit');
INSERT INTO lims.biospecimen_species (name) VALUES ('Rat');
INSERT INTO lims.biospecimen_species (name) VALUES ('Sheep');

-- status
INSERT INTO lims.biospecimen_status (name) VALUES ('New');
INSERT INTO lims.biospecimen_status (name) VALUES ('Archived');
