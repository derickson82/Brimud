create procedure populateAllPkey ()
BEGIN

DECLARE done INT DEFAULT 0;
DECLARE rId char(30);
DECLARE zId char(30);
DECLARE r_cursor CURSOR for select id, zone_id from room;
DECLARE CONTINUE handler for not found set done = 1;

open r_cursor;

read_loop: LOOP
  fetch r_cursor into rId, zId;
  if done THEN
    LEAVE read_loop;
  END IF
  call populatePkey(rId, zId);
end LOOP;

close r_cursor;

END;