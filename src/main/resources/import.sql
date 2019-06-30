INSERT INTO user (first_name, last_name) VALUES ('Lukasz', 'Pociecha')
INSERT INTO user (first_name, last_name) VALUES ('John', 'Doe')

INSERT INTO tweet (text, user_id) VALUES ('Lukasz first tweet', 1)
INSERT INTO tweet (text, user_id) VALUES ('Lukasz second tweet', 1)

INSERT INTO tweet (text, user_id) VALUES ('John Doe first tweet', 2)


INSERT INTO comment (text, user_id) VALUES ('Lukasz first comment', 1)
INSERT INTO comment (text, user_id) VALUES ('Lukasz second comment', 1)
INSERT INTO comment (text, user_id) VALUES ('John Doe first comment', 2)

INSERT INTO tweet_comments VALUES (1, 1)
INSERT INTO tweet_comments VALUES (1, 2)
INSERT INTO tweet_comments VALUES (2, 3)