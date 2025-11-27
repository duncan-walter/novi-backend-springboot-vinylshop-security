-- Genre data
INSERT INTO genres (name, description, created_date, modified_date)
VALUES ('Halloween', 'Very spooky', NOW(), NOW()),
       ('Disco', 'Boogie woogie', NOW(), NOW()),
       ('Rock', 'Rock-''n-roll', NOW(), NOW()),
       ('Classical', '7th symphony', NOW(), NOW()),
       ('70''s', 'Funky!', NOW(), NOW());

-- Publisher data
INSERT INTO publishers (name, address, created_date, modified_date)
VALUES ('Spooky records', '1 Spooky avenue', NOW(), NOW()),
       ('Boogie Blast Publishing', '99 Disco Street', NOW(), NOW()),
       ('Electric Records', '42 Electric Avenue', NOW(), NOW()),
       ('Artemis Classical Press', '12 Maestro Lane', NOW(), NOW()),
       ('RetroGroove Media', '70s Funk Boulevard', NOW(), NOW());