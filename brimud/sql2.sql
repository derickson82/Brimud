

--alter table zone add column pkey varchar(40);
--alter table room add column pkey varchar(40);
--alter table room add column zone_fkey varchar(40);
--alter table exits add column room_pkey varchar(40);
--alter table exits add column exit_fkey varchar(40);
--alter table extras add column room_pkey varchar(40);
--alter table player add column room_pkey varchar(40);
--alter table scans add column room_pkey varchar(40);

alter table room drop foreign key fk_new_room_zone;
alter table zone drop index idx_zone_pkey;

alter table extras drop foreign key fk_new_extras_room;
alter table scans drop foreign key fk_new_scans_room;
alter table exits drop foreign key fk_new_exits_room;
alter table player drop foreign key fk_new_player_room;
alter table room drop index idx_room_pkey;

create index idx_zone_pkey on zone(pkey);
create index idx_room_pkey on room(pkey);
alter table room add constraint fk_new_room_zone FOREIGN KEY (zone_fkey) REFERENCES zone(pkey);
alter table player add constraint fk_new_player_room FOREIGN KEY (room_pkey) REFERENCES room(pkey);
alter table exits add constraint fk_new_exits_room FOREIGN KEY (room_pkey) REFERENCES room(pkey);
alter table scans add constraint fk_new_scans_room FOREIGN KEY (room_pkey) REFERENCES room(pkey);
ALTER TABLE extras ADD CONSTRAINT fk_new_extras_room FOREIGN KEY (room_pkey) REFERENCES room(pkey);

--ALTER TABLE player ADD CONSTRAINT fk_player_zone FOREIGN KEY (zone_id) REFERENCES zone(id);
--ALTER TABLE player ADD CONSTRAINT fk_player_pcharacter FOREIGN KEY (pcharacter) REFERENCES pcharacter(name);
--ALTER TABLE room ADD CONSTRAINT fk_room_zone FOREIGN KEY (zone_id) REFERENCES zone(id);

--ALTER TABLE zone ADD CONSTRAINT fk_zone_starting FOREIGN KEY (starting_room, starting_zone_id) REFERENCES room(id, zone_id);

--ALTER TABLE exits ADD CONSTRAINT fk_exits_room FOREIGN KEY (owning_room_id, owning_zone_id) REFERENCES room(id, zone_id);
--ALTER TABLE exits ADD CONSTRAINT fk_exits_direction FOREIGN KEY (direction) REFERENCES direction(direction);
--ALTER TABLE scans ADD CONSTRAINT fk_scans_room FOREIGN KEY (owning_room_id, owning_zone_id) REFERENCES room(id, zone_id);
--ALTER TABLE scans ADD CONSTRAINT fk_scans_direction FOREIGN KEY (direction) REFERENCES direction(direction);
--ALTER TABLE extras ADD CONSTRAINT fk_extras_room FOREIGN KEY (owning_room_id, owning_zone_id) REFERENCES room(id, zone_id);




