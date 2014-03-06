CREATE TABLE IF NOT EXISTS account (
  name VARCHAR(30) NOT NULL,
  password VARCHAR(256) NOT NULL,
  salt VARCHAR(40) NOT NULL,
  CONSTRAINT pk_account PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS player (
	name VARCHAR(30) NOT NULL,
	pcharacter VARCHAR(30),
	room_id VARCHAR(30),
	zone_id VARCHAR(30),
	CONSTRAINT pk_player PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS pcharacter (
  name VARCHAR(30) NOT NULL,
  CONSTRAINT pk_pcharacter PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS zone (
  id VARCHAR(30) NOT NULL,
  name VARCHAR(30) NOT NULL,
  description TEXT NOT NULL,
  starting_zone BOOLEAN NOT NULL DEFAULT false,
  starting_zone_id VARCHAR(30) NULL,
  starting_room VARCHAR(30) NULL,
  CONSTRAINT pk_zone PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS room (
  id VARCHAR(30) NOT NULL,
  zone_id VARCHAR(30) NOT NULL,
  name VARCHAR(256) NOT NULL DEFAULT 'Room needs name',
  short VARCHAR(256) NOT NULL DEFAULT 'You see nothing of interest.',
  description TEXT NOT NULL,
  CONSTRAINT pk_room PRIMARY KEY (id, zone_id)
);

CREATE TABLE IF NOT EXISTS exits (
	owning_room_id VARCHAR(30) NOT NULL,
	owning_zone_id VARCHAR(30) NOT NULL,
	direction VARCHAR(6) NOT NULL,
	room_id VARCHAR(30) NOT NULL,
	zone_id VARCHAR(30) NOT NULL,
	CONSTRAINT pk_exits PRIMARY KEY (owning_room_id, owning_zone_id, direction)
);

CREATE TABLE IF NOT EXISTS scans (
	owning_room_id VARCHAR(30) NOT NULL,
	owning_zone_id VARCHAR(30) NOT NULL,
	direction VARCHAR(6) NOT NULL,
	description VARCHAR(256) NOT NULL,
	CONSTRAINT pk_scans PRIMARY KEY (owning_room_id, owning_zone_id, direction)
);

CREATE TABLE IF NOT EXISTS extras (
	owning_room_id VARCHAR(30) NOT NULL,
	owning_zone_id VARCHAR(30) NOT NULL,
	keyword VARCHAR(30) NOT NULL,
	description VARCHAR(256) NOT NULL,
	CONSTRAINT pk_extras PRIMARY KEY (owning_room_id, owning_zone_id, keyword)
);

CREATE TABLE IF NOT EXISTS direction (
	direction VARCHAR(6),
	PRIMARY KEY (direction)
);

ALTER TABLE player ADD CONSTRAINT fk_player_room FOREIGN KEY (room_id, zone_id) REFERENCES room(id, zone_id);
ALTER TABLE player ADD CONSTRAINT fk_player_zone FOREIGN KEY (zone_id) REFERENCES zone(id);
ALTER TABLE player ADD CONSTRAINT fk_player_pcharacter FOREIGN KEY (pcharacter) REFERENCES pcharacter(name);
ALTER TABLE room ADD CONSTRAINT fk_room_zone FOREIGN KEY (zone_id) REFERENCES zone(id);
ALTER TABLE exits ADD CONSTRAINT fk_exits_room FOREIGN KEY (owning_room_id, owning_zone_id) REFERENCES room(id, zone_id);
ALTER TABLE exits ADD CONSTRAINT fk_exits_direction FOREIGN KEY (direction) REFERENCES direction(direction);
ALTER TABLE scans ADD CONSTRAINT fk_scans_room FOREIGN KEY (owning_room_id, owning_zone_id) REFERENCES room(id, zone_id);
ALTER TABLE scans ADD CONSTRAINT fk_scans_direction FOREIGN KEY (direction) REFERENCES direction(direction);
ALTER TABLE extras ADD CONSTRAINT fk_extras_room FOREIGN KEY (owning_room_id, owning_zone_id) REFERENCES room(id, zone_id);

INSERT INTO direction values ('NORTH');
INSERT INTO direction values ('SOUTH');
INSERT INTO direction values ('EAST');
INSERT INTO direction values ('WEST');
INSERT INTO direction values ('UP');
INSERT INTO direction values ('DOWN');

