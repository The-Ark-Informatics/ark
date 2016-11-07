DROP FUNCTION IF EXISTS `study`.`udf_FirstNumberPos`;
DELIMITER ;;
CREATE FUNCTION `study`.`udf_FirstNumberPos` (`instring` varchar(4000)) 
RETURNS int
LANGUAGE SQL
DETERMINISTIC
NO SQL
SQL SECURITY INVOKER
BEGIN
	DECLARE position int;
	DECLARE tmp_position int;
	SET position = 5000;
	SET tmp_position = LOCATE('0', instring); IF (tmp_position > 0 AND tmp_position < position) THEN SET position = tmp_position; END IF; 
	SET tmp_position = LOCATE('1', instring); IF (tmp_position > 0 AND tmp_position < position) THEN SET position = tmp_position; END IF;
	SET tmp_position = LOCATE('2', instring); IF (tmp_position > 0 AND tmp_position < position) THEN SET position = tmp_position; END IF;
	SET tmp_position = LOCATE('3', instring); IF (tmp_position > 0 AND tmp_position < position) THEN SET position = tmp_position; END IF;
	SET tmp_position = LOCATE('4', instring); IF (tmp_position > 0 AND tmp_position < position) THEN SET position = tmp_position; END IF;
	SET tmp_position = LOCATE('5', instring); IF (tmp_position > 0 AND tmp_position < position) THEN SET position = tmp_position; END IF;
	SET tmp_position = LOCATE('6', instring); IF (tmp_position > 0 AND tmp_position < position) THEN SET position = tmp_position; END IF;
	SET tmp_position = LOCATE('7', instring); IF (tmp_position > 0 AND tmp_position < position) THEN SET position = tmp_position; END IF;
	SET tmp_position = LOCATE('8', instring); IF (tmp_position > 0 AND tmp_position < position) THEN SET position = tmp_position; END IF;
	SET tmp_position = LOCATE('9', instring); IF (tmp_position > 0 AND tmp_position < position) THEN SET position = tmp_position; END IF;

	IF (position = 5000) THEN RETURN 0; END IF;
		RETURN position;
END
;;
DELIMITER ;

DROP FUNCTION IF EXISTS `study`.`udf_NaturalSortFormat`;
DELIMITER ;;
CREATE FUNCTION `study`.`udf_NaturalSortFormat` (`instring` varchar(4000), `numberLength` int, `sameOrderChars` char(50)) 
RETURNS varchar(4000)
LANGUAGE SQL
DETERMINISTIC
NO SQL
SQL SECURITY INVOKER
BEGIN
	DECLARE sortString varchar(4000);
	DECLARE numStartIndex int;
	DECLARE numEndIndex int;
	DECLARE padLength int;
	DECLARE totalPadLength int;
	DECLARE i int;
	DECLARE sameOrderCharsLen int;

	SET totalPadLength = 0;
	SET instring = REPLACE(TRIM(instring), ' ', '');
	SET sortString = instring;
	SET numStartIndex = study.udf_FirstNumberPos(instring);
	SET numEndIndex = 0;
	SET i = 1;
	SET sameOrderCharsLen = LENGTH(sameOrderChars);

	WHILE (i <= sameOrderCharsLen) DO
		SET sortString = REPLACE(sortString, SUBSTRING(sameOrderChars, i, 1), ' ');
		SET i = i + 1;
	END WHILE;

	# SET sortString = REPLACE(sortString, ' ', '');

	WHILE (numStartIndex <> 0) DO
		SET numStartIndex = numStartIndex + numEndIndex;
		SET numEndIndex = numStartIndex;

		WHILE (study.udf_FirstNumberPos(SUBSTRING(instring, numEndIndex, 1)) = 1) DO
			SET numEndIndex = numEndIndex + 1;
		END WHILE;

		SET numEndIndex = numEndIndex - 1;

		SET padLength = numberLength - (numEndIndex + 1 - numStartIndex);

		IF padLength < 0 THEN
			SET padLength = 0;
		END IF;

		SET sortString = INSERT(sortString, numStartIndex + totalPadLength, 0, REPEAT('0', padLength));

		SET totalPadLength = totalPadLength + padLength;
		SET numStartIndex = study.udf_FirstNumberPos(RIGHT(instring, LENGTH(instring) - numEndIndex));
	END WHILE;

	RETURN sortString;
END
;;
DELIMITER ;

