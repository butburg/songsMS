INSERT INTO User (userId, firstspringbootname, lastname, password) VALUES ('mmuster', 'Maxime', 'Muster', 'pass1234');
INSERT INTO User (userid, firstname, lastname, password) VALUES ('eschuler','Elena','Schuler','pass1234');
INSERT INTO User (userid, firstname, lastname, password) VALUES ('eneuman','Erwin','Neumann','pass1234');
INSERT INTO User (userid, firstname, lastname, password) VALUES ('psalto','Phine','Salto','pass1234');

INSERT INTO Song (id, title, artist, label, released) VALUES (1, 'We Built This City','Starship','Grunt/RCA',1985);
INSERT INTO Song (id, title, artist, label, released) VALUES (2, 'Sussudio','Phil Collins','Virgin',1985);

INSERT INTO SongList (id, ownerId, name, isPrivate) VALUES (1, 'mmuster','mmusterPrivate',true);
INSERT INTO SongList (id, ownerId, name, isPrivate) VALUES (2, 'mmuster','mmusterPublic',false);

INSERT INTO SongList (id, ownerId, name, isPrivate) VALUES (3, 'eschuler','eschulerPrivate',true);
INSERT INTO SongList (id, ownerId, name, isPrivate) VALUES (4, 'eschuler','eschulerPublic',false);

INSERT INTO SongList_Song (songList_id, song_id) VALUES (1,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (1,2);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (2,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (2,2);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (3,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (3,2);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (4,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (4,2);