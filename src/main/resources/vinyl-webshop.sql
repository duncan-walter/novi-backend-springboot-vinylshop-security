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