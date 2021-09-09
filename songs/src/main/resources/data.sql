INSERT INTO Song (title, artist, label, released) VALUES ('We Built This City','Starship','Grunt/RCA',1985);
INSERT INTO Song (title, artist, label, released) VALUES ('Sussudio','Phil Collins','Virgin',1985);

INSERT INTO SongList (ownerId, name, isPrivate) VALUES ('mmuster','mmusterPrivate',true);
INSERT INTO SongList (ownerId, name, isPrivate) VALUES ('mmuster','mmusterPublic',false);

INSERT INTO SongList (ownerId, name, isPrivate) VALUES ('eschuler','eschulerPrivate',true);
INSERT INTO SongList (ownerId, name, isPrivate) VALUES ('eschuler','eschulerPublic',false);

INSERT INTO SongList_Song (songList_id, song_id) VALUES (1,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (1,2);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (2,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (2,2);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (3,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (3,2);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (4,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (4,2);