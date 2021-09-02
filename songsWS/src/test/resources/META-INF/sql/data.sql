INSERT INTO User (userId, firstname, lastname, password) VALUES ('psaal', 'Philipp', 'Saal', 'pw12');
INSERT INTO User (userid, firstname, lastname, password) VALUES ('ewiese','Edwin','Wiese','pw12');

INSERT INTO Song (id, title, artist, label, released) VALUES (1, 'MONTERO','Lil Nas X','Columbia Records',2021);
INSERT INTO Song (id, title, artist, label, released) VALUES (2, 'The Business','Tiesto','Musical Freedom',2020);

INSERT INTO SongList (id, ownerId, name, isPrivate) VALUES (1, 'psaal','saalPrivat',true);
INSERT INTO SongList (id, ownerId, name, isPrivate) VALUES (2, 'psaal','saalPublic',false);

INSERT INTO SongList (id, ownerId, name, isPrivate) VALUES (3, 'ewiese','wiesePrivate',true);
INSERT INTO SongList (id, ownerId, name, isPrivate) VALUES (4, 'ewiese','wiesePublic',false);

INSERT INTO SongList_Song (songList_id, song_id) VALUES (1,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (1,2);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (2,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (2,2);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (3,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (3,2);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (4,1);
INSERT INTO SongList_Song (songList_id, song_id) VALUES (4,2);