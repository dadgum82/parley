# Load the Users
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

# Load the Chat Rooms
INSERT INTO chat_rooms (id, icon_path, moderator_id, name)
VALUES (1, null, 1, 'Poker Night');


# Add Users to Chat Rooms
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

# Add Messages to Chat Rooms
-- Planning Before Poker Night
-- Planning Before Poker Night
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (1, 1, 1, 'Hey guys, I''m hosting a poker night at my place this Friday. Who''s in?', '2023-12-08 18:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (2, 1, 2, 'Waffles, count me in! I''ve been practicing my poker face.', '2023-12-08 18:15:03');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (3, 1, 3, 'Poker night sounds pawsome! I''m ready to bring my A-game.', '2023-12-08 18:30:20');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (4, 1, 4, 'Woof, count me too! Got my lucky collar ready.', '2023-12-08 18:48:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (5, 1, 5, 'I''m in! Can we have treats too?', '2023-12-08 19:10:23');

-- Texting During Poker Night
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (6, 1, 1, 'Welcome, everyone! Grab a seat, and let the games begin. 🐾', '2023-12-08 20:13:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (7, 1, 2, 'Woof, this poker face is harder than it looks!', '2023-12-08 20:17:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (8, 1, 3, 'Sniffing out bluffs like a pro. Got my poker tail wag ready.', '2023-12-08 20:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (9, 1, 4, 'My lucky collar is working, feeling luckier than ever!', '2023-12-08 20:45:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (10, 1, 5, 'Woof, can we take a break for treats? I''m getting hungry.', '2023-12-08 21:00:00');

-- Continuation of Poker Night
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (11, 1, 1, 'Break time! Treats for everyone. Fuel up for the next round.', '2023-12-08 21:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (12, 1, 2, 'These treats are a game-changer. Ready to win big now.', '2023-12-08 21:45:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (13, 1, 3, 'Strategy session: who''s the toughest opponent here?', '2023-12-08 22:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (14, 1, 4, 'No one can beat my paw-some poker skills!', '2023-12-08 22:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (15, 1, 5, 'I might be a dark horse in this game. Watch out!', '2023-12-08 22:30:00');

-- Closing Moments of Poker Night
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (16, 1, 1, 'Final round, everyone! Winner takes all the tennis balls.', '2023-12-08 23:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (17, 1, 2, 'It''s getting intense. My poker tail wag is at maximum speed.', '2023-12-08 23:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (18, 1, 3, 'Sniffed out a bluff! Victory is near.', '2023-12-08 23:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (19, 1, 4, 'Lucky collar, don''t fail me now!', '2023-12-08 23:45:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (20, 1, 5, 'Woof, I fold. Congrats to the winner!', '2023-12-09 00:00:00');

-- Thoughts After Poker Night
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (21, 1, 1, 'Thanks for coming, everyone! Poker night was a blast.', '2023-12-09 00:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (22, 1, 2, 'Can we do this every week? I love a good challenge.', '2023-12-09 01:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (23, 1, 3, 'Next time, I''m bringing my poker sunglasses.', '2023-12-09 01:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (24, 1, 4, 'Lucky collar, you''re my forever charm!', '2023-12-09 02:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (25, 1, 5, 'Woof, I had a great time. Treats and poker, the best of both worlds.', '2023-12-09 02:30:00');

-- Extended Story

-- Additional Poker Night Shenanigans
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (26, 1, 1, 'Who''s up for a midnight round? Double or nothing!', '2023-12-09 03:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (27, 1, 2, 'I''m in, Waffles! Midnight poker, the excitement never ends.', '2023-12-09 03:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (28, 1, 3, 'Midnight poker? Count me in. My poker senses are still sharp!', '2023-12-09 03:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (29, 1, 4, 'Woof, I''m sleepy, but I can''t resist. Let''s do it!', '2023-12-09 03:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (30, 1, 5, 'Midnight treats too? I''m definitely in!', '2023-12-09 04:00:00');

-- Midnight Poker Action
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (31, 1, 1, 'Alright, midnight crew! Let the poker madness continue.', '2023-12-09 04:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (32, 1, 2, 'My poker tail wag is now on overdrive. Brace yourselves!', '2023-12-09 04:45:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (33, 1, 3, 'Sniffing out bluffs in the dark. This is the real challenge!', '2023-12-09 05:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (34, 1, 4, 'Woof, someone turn on the lights! I can''t see my cards.', '2023-12-09 05:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (35, 1, 5, 'Midnight treats acquired. Let the poker victories begin!', '2023-12-09 05:30:00');

-- Reflecting on Midnight Poker
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (36, 1, 1, 'That was wild! Midnight poker is officially a tradition.', '2023-12-09 06:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (37, 1, 2, 'I''ve never had so much fun losing at poker. Count me in for next time!', '2023-12-09 06:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (38, 1, 3, 'Midnight poker should be a monthly thing. What do you think, guys?', '2023-12-09 07:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (39, 1, 4, 'Woof, my fur needs a break, but I''m already looking forward to the next one!',
        '2023-12-09 07:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (40, 1, 5, 'Midnight treats and poker. The perfect way to end the night!', '2023-12-09 08:00:00');

-- Create Chat Room for Dog Owners
INSERT INTO chat_rooms (id, icon_path, moderator_id, name)
VALUES (2, null, 6, 'Guardians'' of the Good Dogs');

-- Add Owners to the Chat Room
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (2, 6); -- Jesse
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (2, 7); -- Bryan
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (2, 8);
-- Jason

-- Start the Conversation
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (41, 2, 6, 'Hey Bryan and Jason! Wondering what our furry friends are up to?', '2023-12-09 09:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (42, 2, 7, 'Hey Jesse and Jason! Luna''s been acting a bit mysterious lately. Any clues?',
        '2023-12-09 09:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (43, 2, 8, 'Hey Jesse and Bryan! Muffins had a long nap today. What about Waffles and Luna?',
        '2023-12-09 09:30:00');

-- Speculations about Dogs' Activities
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (44, 2, 6, 'Waffles was practicing poker last night. Maybe Luna and Muffins joined the game?',
        '2023-12-09 10:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (45, 2, 7, 'Luna with a poker face? That''d be a sight! What do you think, Jason?', '2023-12-09 10:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (46, 2, 8,
        'Muffins is more of a treat lover than a card player, but who knows! Jesse, any poker tales from Waffles?',
        '2023-12-09 10:30:00');

-- Wrapping Up the Conversation
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (47, 2, 6, 'Waffles did mention treats during the game. Maybe they had a snack break!', '2023-12-09 11:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (48, 2, 7, 'Haha, Luna probably tried to eat the poker chips. Dogs and snacks, classic!', '2023-12-09 11:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (49, 2, 8, 'Well, whatever they''re up to, it sounds like they''re having fun. Doggy mysteries!',
        '2023-12-09 11:30:00');

# Random chat

-- Create Chat Room with Random Owners and Dogs
INSERT INTO chat_rooms (id, icon_path, moderator_id, name)
VALUES (3, null, 6, 'Dudes and Dogs');

-- Add Users to the Chat Room
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (3, 1); -- Waffles
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (3, 2); -- Luna
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (3, 3); -- Spike
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (3, 4); -- Koshka
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (3, 5); -- Muffins
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (3, 6); -- Jesse
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (3, 7); -- Bryan
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (3, 8);
-- Jason

-- Generate Random Chat Messages
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (50, 3, 1, 'Hey everyone! What''s the latest gossip in the dog world?', '2023-12-09 12:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (51, 3, 2, 'Luna found a mysterious squeaky toy. Any guesses where it came from?', '2023-12-09 12:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (52, 3, 3, 'Spike has been digging up the backyard. I hope he''s not hiding treasure!', '2023-12-09 12:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (53, 3, 4, 'Koshka brought a feather inside. Now it''s a cat-and-dog game!', '2023-12-09 12:45:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (54, 3, 5, 'Muffins tried to chase a butterfly. The butterfly won.', '2023-12-09 13:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (55, 3, 6, 'Jesse, did Waffles discover any new trails on your last hike?', '2023-12-09 13:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (56, 3, 7, 'Bryan, Luna and Waffles need a playdate soon. They miss each other!', '2023-12-09 13:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (57, 3, 8, 'Jason, any funny antics from Muffins lately? Share the laughs!', '2023-12-09 13:45:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (58, 3, 1, 'Waffles found a mysterious stick in the backyard. Investigation in progress!',
        '2023-12-09 14:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (59, 3, 2, 'Luna is on squirrel patrol. Every tree is a potential hiding spot!', '2023-12-09 14:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (60, 3, 3, 'Spike stole my sock again. Sock thief in the house!', '2023-12-09 14:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (61, 3, 4, 'Koshka is having a staring contest with the neighbor''s cat. Epic showdown!', '2023-12-09 14:45:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (62, 3, 5, 'Muffins is sunbathing. Living the good life!', '2023-12-09 15:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (63, 3, 6, 'Jesse, Waffles thinks he''s a detective. He''s following mysterious paw prints.',
        '2023-12-09 15:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (64, 3, 7, 'Bryan, Luna wants to challenge Waffles to a race. Who''s the fastest pup?', '2023-12-09 15:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (65, 3, 8, 'Jason, Muffins had a playdate with a butterfly. Fluttery fun!', '2023-12-09 15:45:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (66, 3, 1, 'Waffles brought the mysterious stick inside. Now it''s a prized possession!', '2023-12-09 16:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (67, 3, 2, 'Luna claims victory in the squirrel patrol. No acorn left behind!', '2023-12-09 16:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (68, 3, 3, 'Spike and the sock are in a standoff. The sock might win this time.', '2023-12-09 16:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (69, 3, 4, 'Koshka won the staring contest. Victory meow!', '2023-12-09 16:45:00');

#Emoji Tests

-- Create Chat Room for Emoji Test
INSERT INTO chat_rooms (id, icon_path, moderator_id, name)
VALUES (4, null, 1, 'Emoji Testing');

-- Add Users to the Chat Room
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (4, 1); -- Waffles
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (4, 2); -- Luna
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (4, 3); -- Spike
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (4, 4); -- Koshka
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (4, 5); -- Muffins
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (4, 6); -- Jesse
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (4, 7); -- Bryan
INSERT INTO chat_room_users (chat_room_id, user_id)
VALUES (4, 8);
-- Jason

-- Generate Random Chat Messages for Emoji Test
INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (70, 4, 1, 'Hey everyone! 🐾 Let''s test some emojis in this chat. 😊', '2023-12-10 09:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (71, 4, 2, 'Hi there! 🌙 Luna is ready for some emoji fun! 🐶', '2023-12-10 09:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (72, 4, 3, 'Spike here! 🦔 Excited to see how these emojis work! 🎉', '2023-12-10 09:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (73, 4, 4, 'Koshka reporting in! 🐱 Let the emoji party begin! 🎈', '2023-12-10 09:45:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (74, 4, 5, 'Muffins is here too! 🐾 Ready for some emoji magic! ✨', '2023-12-10 10:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (75, 4, 6, 'Jesse and Waffles joining the emoji extravaganza! 🐕🎊', '2023-12-10 10:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (76, 4, 7, 'Bryan here! 👋 Let''s explore the world of emojis! 🌍', '2023-12-10 10:30:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (77, 4, 8, 'Jason checking in! 🤓 Ready to test all the emojis! 🚀', '2023-12-10 10:45:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (78, 4, 1, 'Alright, let''s start with some animal emojis! 🦊🦄🐘', '2023-12-10 11:00:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (79, 4, 2, 'I love the 🌈 emoji! It adds so much color to the chat! 🎨', '2023-12-10 11:15:00');

INSERT INTO chat_messages (id, chat_room_id, user_id, content, timestamp)
VALUES (80, 4, 3, 'Let''s not forget the classic 😂 emoji for a good laugh! 🤣', '2023-12-10 11:30:00');

-- Feel free to add more messages or modify the existing ones as needed.


