CREATE TABLE IF NOT EXISTS pcharacter (
  name VARCHAR(30) NOT NULL,
  CONSTRAINT pk_pcharacter PRIMARY KEY (name)
);

ALTER TABLE player ADD CONSTRAINT fk_player_pcharacter FOREIGN KEY (pcharacter) REFERENCES pcharacter(name);
