-- NOTE
-- The VALUES lines in the INSERT INTO statements are placed on a single line despite some of them being very long and hard to read.
-- This is to keep the file somewhat compact, but it can get very hard when reading certain INSERT INTO statements where foreign keys are being selected.

-- Genre data
INSERT INTO genres (name, description, created_date, modified_date)
VALUES ('Halloween', 'Very spooky', NOW(), NOW()),
       ('Disco', 'Boogie woogie', NOW(), NOW()),
       ('Rock', 'Rock-''n-roll', NOW(), NOW()),
       ('Classical', '7th symphony', NOW(), NOW()),
       ('70''s', 'Funky!', NOW(), NOW());

-- Publisher data
INSERT INTO publishers (name, address, contact_details, created_date, modified_date)
VALUES ('Spooky records', '1 Spooky avenue', '+1 217-703-2085', NOW(), NOW()),
       ('Boogie Blast Publishing', '99 Disco Street', '+1 705-680-3365', NOW(), NOW()),
       ('Electric Records', '42 Electric Avenue', '+1 644-580-8106', NOW(), NOW()),
       ('Artemis Classical Press', '12 Maestro Lane', '+1 548-451-1570', NOW(), NOW()),
       ('RetroGroove Media', '70s Funk Boulevard', '+1 303-823-7949', NOW(), NOW());

-- Artist data
INSERT INTO artists (name, biography, created_date, modified_date)
VALUES ('Mr. Skeleton', 'He has lived his year spooking many people with his music, now he''s dead. Serves him right.', NOW(), NOW()),
       ('DJ Disco Inferno', 'Famous for lighting up the disco floors in the 70s, and still spinning hot tracks.', NOW(), NOW()),
       ('Rocky Thunder', 'Lead guitarist of many legendary rock bands, known for epic solos.', NOW(), NOW()),
       ('Maestro Allegro', 'Classical virtuoso and conductor of symphonies across Europe.', NOW(), NOW()),
       ('Funky Joe', '70s funk legend, bass player extraordinaire with a groovy style.', NOW(), NOW());

-- Album data
INSERT INTO albums (title, release_year, genre_id, publisher_id, created_date, modified_date)
VALUES ('The Nightmare Before Christmas', 1993, (SELECT id FROM genres WHERE name = 'Halloween'), (SELECT id FROM publishers WHERE name = 'Spooky records'), NOW(), NOW()),
       ('Disco Fever', 1978, (SELECT id FROM genres WHERE name = 'Disco'), (SELECT id FROM publishers WHERE name = 'Boogie Blast Publishing'), NOW(), NOW()),
       ('Thunderstrike', 1985, (SELECT id FROM genres WHERE name = 'Rock'), (SELECT id FROM publishers WHERE name = 'Electric Records'), NOW(), NOW()),
       ('Symphony No.7 in D', 1877, (SELECT id FROM genres WHERE name = 'Classical'), (SELECT id FROM publishers WHERE name = 'Artemis Classical Press'), NOW(), NOW()),
       ('Funky Town Revival', 1975, (SELECT id FROM genres WHERE name = '70''s'), (SELECT id FROM publishers WHERE name = 'RetroGroove Media'), NOW(), NOW());

-- Arist-album data
INSERT INTO albums_artists (album_id, artist_id)
VALUES ((SELECT id FROM albums WHERE title = 'The Nightmare Before Christmas'), (SELECT id FROM artists WHERE name = 'Mr. Skeleton')),
       ((SELECT id FROM albums WHERE title = 'Disco Fever'), (SELECT id FROM artists WHERE name = 'DJ Disco Inferno')),
       ((SELECT id FROM albums WHERE title = 'Disco Fever'), (SELECT id FROM artists WHERE name = 'Rocky Thunder')),
       ((SELECT id FROM albums WHERE title = 'Thunderstrike'), (SELECT id FROM artists WHERE name = 'Rocky Thunder')),
       ((SELECT id FROM albums WHERE title = 'Symphony No.7 in D'), (SELECT id FROM artists WHERE name = 'Maestro Allegro')),
       ((SELECT id FROM albums WHERE title = 'Funky Town Revival'), (SELECT id FROM artists WHERE name = 'Funky Joe')),
       ((SELECT id FROM albums WHERE title = 'Funky Town Revival'), (SELECT id FROM artists WHERE name = 'DJ Disco Inferno'));

-- Stock data
INSERT INTO stocks (condition, price, album_id, created_date, modified_date)
VALUES ('Used', 10, (SELECT id FROM albums WHERE title = 'The Nightmare Before Christmas'), NOW(), NOW()),
       ('Mint', 25, (SELECT id FROM albums WHERE title = 'Disco Fever'), NOW(), NOW()),
       ('Near Mint', 20, (SELECT id FROM albums WHERE title = 'Thunderstrike'), NOW(), NOW()),
       ('Brand New', 50, (SELECT id FROM albums WHERE title = 'Symphony No.7 in D'), NOW(), NOW()),
       ('Good', 15, (SELECT id FROM albums WHERE title = 'Funky Town Revival'), NOW(), NOW());