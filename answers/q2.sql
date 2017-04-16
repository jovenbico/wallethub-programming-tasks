-- DROP FUNCTION IF EXISTS hello;

DELIMITER $$
CREATE FUNCTION INITCAP (input VARCHAR(255)) RETURNS VARCHAR(255)
BEGIN
	DECLARE result VARCHAR(255);
	DECLARE length INT;
	DECLARE position INT;
	DECLARE current_char VARCHAR(1);
	DECLARE will_upper BOOLEAN;

	SET position = 1;
	SET result = UPPER(SUBSTRING(input, position, 1));
	SET length = LENGTH(input);

	-- /*
	WHILE position < length DO

		SET position = position + 1;
		SET current_char = SUBSTRING(input, position, 1);
		
		IF current_char = " " THEN 
			SET will_upper = TRUE;
		ELSEIF will_upper THEN
			SET current_char = UPPER(current_char);
			SET will_upper = FALSE;
		ELSE 
			SET current_char = LOWER(current_char);
		END IF;

		SET result = CONCAT(result, current_char);

	END WHILE;
	-- */
	RETURN result;
END$$

DELIMITER ;
