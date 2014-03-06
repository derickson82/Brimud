DELIMITER $$
DROP PROCEDURE IF EXISTS `brimud`.`populatePkey`$$
CREATE DEFINER=`brimud`@`localhost` PROCEDURE `populatePkey`(
  IN roomId varchar(30), 
  IN zoneId varchar(30)
 )
BEGIN
  DECLARE room_uuid varchar(40);
  select uuid() into room_uuid;
  
  UPDATE room 
  set pkey = room_uuid 
  where id = roomId 
  and zone_id = zoneId;

  update exits
  set room_pkey = room_uuid
  where owning_room_id = roomId
  and owning_zone_id = zoneId;

  update exits
  set exit_fkey = room_uuid
  where room_id = roomId
  and zone_id = zoneId;
  
  update extras
  set room_pkey = room_uuid
  where owning_room_id = roomId
  and owning_zone_id = zoneId;

  update player
  set room_pkey = room_uuid
  where room_id = roomId
  and zone_id = zoneId;

  update scans
  set room_pkey = room_uuid
  where owning_room_id = roomId
  and owning_zone_id = zoneId;

END$$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `brimud`.`popAllKey`$$
CREATE DEFINER=`brimud`@`localhost` PROCEDURE `popAllKey`()
BEGIN
  DECLARE done INT DEFAULT 0;
  DECLARE rId char(30);
  DECLARE zId char(30);
  DECLARE var_pkey char(40);
  DECLARE r_cursor CURSOR for select id, zone_id, pkey from room;
  DECLARE CONTINUE handler for not found set done = 1;

  OPEN r_cursor;

  room_loop: LOOP
    fetch r_cursor into rId, zId, var_pkey;
    if done THEN
      LEAVE room_loop;
    END IF;
    
    IF var_pkey is null then
    	call populatePkey(rId, zId);
    end if;
  end LOOP;

  close r_cursor;

END$$

DELIMITER ;

DELIMITER $$

DROP PROCEDURE IF EXISTS `brimud`.`popAllZoneKey`$$
CREATE DEFINER=`brimud`@`localhost` PROCEDURE `popAllZoneKey`()
BEGIN
  DECLARE done INT DEFAULT 0;
  DECLARE zId char(30);
  DECLARE var_pkey char(40);
  DECLARE z_cursor CURSOR for select id, pkey from zone;
  DECLARE CONTINUE handler for not found set done = 1;

  OPEN z_cursor;
  zone_loop: LOOP
    fetch z_cursor into zId, var_pkey;
    if done THEN
      LEAVE zone_loop;
    END IF;
    if var_pkey is null then
    	update zone set pkey = (select uuid()) where id = zId;
    end if;
	
  END LOOP;

  close z_cursor;

END$$

DELIMITER ;

