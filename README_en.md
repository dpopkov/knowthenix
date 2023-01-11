# Knowthenix

_The application is under development._

Translations:  
* [Russian](README.md)

### Contents
* [General Info](#general-info)
* [Used Technologies](#used-technologies)
* [How to Launch Manually](#how-to-launch-manually)
* [How to make use of Front-end](#how-to-make-use-of-front-end-2-options)
* [Screenshots](#screenshots)
* [Database Schema](#database-schema)
* [Custom application properties and profiles](#custom-application-properties-and-profiles)
* [Curl requests](#curl-requests)

### General Info

The application serves to maintain the Knowledge Base filled by authorized users.
It supports storage of units of information including matching one Question with several possible Answers using 
different Sources.
The text of each Question or Answer can be presented in several languages.
This Knowledge Base is planned for later use as a source for generating records for the spaced repetition system
(not implemented yet).

### Used Technologies
* Back-end
    * Spring Boot
    * Spring Data JPA
    * Spring MVC
    * H2 database
    * PostgreSQL, MySQL (or other RDBMS with provided drivers)
* Front-end
    * [Knowthenix-ang](https://github.com/dpopkov/knowthenix-ang) - project built using Angular.

### How to Launch Manually
This section is not finished yet. These instructions are mostly for personal use.
There is no guarantee that they will produce expected result on any machine.
They need to be tested and clarified.
* Build: `mvn clean package`
* Create database in PostgreSQL or MySQL: `create database knowthenix-prod`
* Provide environment variables for passing credentials to the application:
    * `KNOWTHENIX_ADMIN=<enter-db-user-name>`
    * `KNOWTHENIX_ADMIN_PASSWORD=<enter-db-user-password>`
* Test connection using any SQL client:
    * Postgres: `jdbc:postgresql://localhost:5432/knowthenix-prod`
    * or MySQL: `jdbc:mysql://localhost:3306/knowthenix-prod?userUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC`
* Run for the first time to populate with some initial data: 
    * `java -jar knowthenix-X.Y.Z-SNAPSHOT.jar --spring.active.profile=init` or `--spring.active.profile=mysqlinit`
    * after initialization there will exist user `admin` with password `admin`
* Run after initializing or run with empty db: 
    * `java -jar knowthenix-X.Y.Z-SNAPSHOT.jar --spring.active.profile=prod`
* Use: 
    * with front-end [Knowthenix-ang](https://github.com/dpopkov/knowthenix-ang).
    * or run curl, httpie, Postman or other client.
    
### How to make use of Front-end (2 options) 
* Pre-condition: Angular CLI should be installed already.
* 1 - start front-end [Knowthenix-ang](https://github.com/dpopkov/knowthenix-ang) with its own server:
    * go to front-end project directory
    * run `ng serve`
    * after starting front-end direct browser to `http://localhost:4200` (provided the back-end is running already)
* 2 - use compiled front-end
    * go to front-end project directory
    * run `ng build --prod` 
    * copy all the compiled files from `dist/knowthenix` directory to `src/main/resources/static` directory of this back-end project
    * run the application using IDE or make jar as usual
        * Important: use application property `--app.security.permitall=true` as temporary measure (will be fixed)
    * direct browser to `http://localhost:8080`

### Screenshots

Screenshots are taken from front-end [Knowthenix-ang](https://github.com/dpopkov/knowthenix-ang)

|  |  |
| ------------- | ------------- |
| <img align="center" src="docs/images/ui-01-register.png" /> | <img align="center" src="docs/images/ui-02-login.png" /> |
| Registration page. If password is not provided, it will be generated. | Login page. Number of login attempts is limited, then user is locked. |
| <img align="center" src="docs/images/ui-03-users.png" /> | <img align="center" src="docs/images/ui-04-settings.png" /> |
| List of all users. | Resetting user password. | 
| <img align="center" src="docs/images/ui-05-edit-user.png" /> | <img align="center" src="docs/images/ui-06-profile.png" /> |
| Edit user. | Profile of current user. |

The rest of screenshots for categories, questions, keyterms and sources will be added later.

### Database Schema
The Entity Relationship diagram is generated in MySQL Workbench. It is not final.
![DB Schema](docs/images/db-schema.png)

### Custom application properties and profiles
Will be added later.

### Curl requests
Will be added later.

[Top](#knowthenix)
