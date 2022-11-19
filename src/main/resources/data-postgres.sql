DELETE FROM category;
INSERT INTO category (id, created_on, modified_on, name, description)
VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Java', 'Java is a high-level, class-based, object-oriented programming language');
INSERT INTO category (id, created_on, modified_on,  name, description)
VALUES (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Spring', 'Spring framework');
INSERT INTO category (id, created_on, modified_on,  name, description)
VALUES (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ORM', 'Object-Relational Mapping');
INSERT INTO category (id, created_on, modified_on,  name, description)
VALUES (4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SQL', 'Structured Query Language');

DELETE FROM keyterm;
INSERT INTO keyterm (id, created_on, modified_on, name, description)
VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'JRE', 'Java Runtime Environment');
INSERT INTO keyterm (id, created_on, modified_on, name, description)
VALUES (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Spring', 'The Spring Framework is an application framework and inversion of control container for the Java platform.');
INSERT INTO keyterm (id, created_on, modified_on, name, description)
VALUES (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Spring 5', 'Spring framework Version 5');
INSERT INTO keyterm (id, created_on, modified_on, name, description)
VALUES (4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Spring 6', 'Spring framework Version 6');
INSERT INTO keyterm (id, created_on, modified_on, name, description)
VALUES (5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Hibernate', 'ORM framework for Java');
INSERT INTO keyterm (id, created_on, modified_on, name, description)
VALUES (6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ORM', 'Object-Relational Mapping, a software programming technique that allows accessing relational databases in the form of abstract objects');
INSERT INTO keyterm (id, created_on, modified_on, name, description)
VALUES (7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Swing', 'Java GUI library');
