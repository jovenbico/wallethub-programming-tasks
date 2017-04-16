-- DROP PROCEDURE IF EXISTS GetAllBeautyTestData;

DELIMITER $$
CREATE PROCEDURE GetAllBeautyTestData()
BEGIN
-- /*
	DECLARE $xname VARCHAR(50);
	DECLARE $vname VARCHAR(50);
	DECLARE $xid INT;
	DECLARE done TINYINT DEFAULT 0;
	DECLARE cur1 CURSOR FOR SELECT id, name FROM test3;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

	create temporary table if not exists _test3 as 
		SELECT id, name FROM test3 limit 0;
	truncate table _test3;

	OPEN cur1;
	READ_LOOP: LOOP
		FETCH FROM cur1 INTO $xid, $xname;
		IF done THEN LEAVE READ_LOOP; END IF;

		SPLIT_LOOP: LOOP
			set $vname = substring($xname, 1, INSTR($xname, '|')-1);
			if $vname = '' or $vname is null then
				insert into _test3(id, name) values ($xid, $xname);
				LEAVE SPLIT_LOOP;
			else
				insert into _test3(id, name) values ($xid, $vname);
				set $xname = replace($xname, concat($vname, '|'), '');
			end if;
		END LOOP;
	END LOOP;
	CLOSE cur1;
-- */
	select id, name from _test3;
END$$
DELIMITER ;