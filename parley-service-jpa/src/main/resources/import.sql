INSERT INTO users (id, name)
VALUES (1, 'Waffles');
INSERT INTO users (id, name)
VALUES (2, 'Luna');
INSERT INTO users (id, name)
VALUES (3, 'Spike');
INSERT INTO users (id, name)
VALUES (4, 'Koshka');
INSERT INTO users (id, name)
VALUES (5, 'Muffins');
INSERT INTO users (id, name)
VALUES (6, 'Jesse');
INSERT INTO users (id, name)
VALUES (7, 'Bryan');
INSERT INTO users (id, name)
VALUES (8, 'Jason');

INSERT INTO chat_rooms (id, icon_path, moderator_id, name)
VALUES (1, null, 1, 'Poker Night');

INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (1, 1);
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (1, 2);
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (1, 3);
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (1, 4);
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (1, 5);
