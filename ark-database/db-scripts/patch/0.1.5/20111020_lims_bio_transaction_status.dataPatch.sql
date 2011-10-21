USE lims;
INSERT INTO `lims`.`bio_transaction_status` (`ID`, `NAME`) VALUES (4, 'Processed');
INSERT INTO `lims`.`bio_transaction_status` (`ID`, `NAME`) VALUES (5, 'Aliquoted');
UPDATE `lims`.`bio_transaction_status` SET name = 'Initial Quantity' WHERE name = 'Initial Qty'
UPDATE `lims`.`bio_transaction` SET reason = 'Initial Quantity' WHERE reason = 'Initial Qty'
