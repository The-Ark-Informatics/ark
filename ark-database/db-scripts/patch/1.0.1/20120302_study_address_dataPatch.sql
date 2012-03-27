-- --------------------------------------------------------------------------------
-- Updates the latest address for all persons and sets to be the "preferred" mailing address
-- --------------------------------------------------------------------------------
USE study;
DELIMITER $$
DROP PROCEDURE IF EXISTS update_address_preferred;
CREATE PROCEDURE `update_address_preferred`(study_id LONG)
BEGIN
	-- Declare variables used just for cursor and loop control
	DECLARE update_id LONG;
	DECLARE no_more_rows BOOLEAN;
	DECLARE loop_cntr INT DEFAULT 0;
	DECLARE num_rows INT DEFAULT 0;
	
	-- Declare the cursor (the latest address)
	DECLARE first_addresses CURSOR FOR
	SELECT max(a.id) as id
	FROM `study`.`address` a. `study`.`person` p, `study`.`link_subject_study` lss
	WHERE a.person_id = p.id
	AND p.id = lss.person_id
	AND lss.study_id = study_id
	GROUP BY a.person_id;
	
	-- Declare 'handlers' for exceptions
	DECLARE CONTINUE HANDLER FOR NOT FOUND
	SET no_more_rows = TRUE;

	IF (study_id IS NOT NULL) THEN
	  	-- 'open' the cursor and capture the number of rows returned
		-- (the 'select' gets invoked when the cursor is 'opened')
		OPEN first_addresses;
		select FOUND_ROWS() into num_rows;
	
		the_loop: LOOP
		    FETCH  first_addresses
		    INTO   update_id;
		
		    -- break out of the loop if
		      -- 1) there were no records, or
		      -- 2) we've processed them all
		    IF no_more_rows THEN
		        CLOSE first_addresses;
		        LEAVE the_loop;
		    END IF;
		
		    UPDATE `study`.`address` SET preferred_mailing_address = 1 WHERE id = update_id;
		
		    -- count the number of times looped
		    SET loop_cntr = loop_cntr + 1;
	  	END LOOP the_loop;
	
		-- 'print' the output so we can see they are the same
		select num_rows, loop_cntr;
	END IF;
END;