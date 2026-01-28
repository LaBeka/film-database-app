# film-database-app
 
## 1 Project-description:
"A Spring Boot backend application designed to manage a movie database, user interactions, and a review system. It features secure Role-Based Access Control to ensure appropriate permissions for public users, authenticated members, and administrators. API Documentation: Fully integrated with Swagger/OpenAPI for easy endpoint testing and exploration".
 
## 2 Technology used
The project uses java running springboot and have implemented a REST-API
The project can be run locally but intends to be running in a docker compose in the cloud

## 3 How to run project (local and docker)

### 3.1 Local
To run the project locally you first need to have a mysql instance running.
If not, download and install the mysql database from the official website.
Project have been tested with versions 8.0 and 8.4. Go into the
*application.properties* file and change the password to your own password 
for the mysql-server before running the application. 

Next login to your mysql server and create an empty database with the
name.
> *film_data_db*

The command is

> *CREATE DATABASE film_data_db;*

If you like copy-paste like me :p.

After this you could either run it through maven or using your ide. The main 
method is located in file *FilmDatabaseApplication.java*. If you run it through an ide
make sure to go into the pom.xml and update the dependencies first.

if you run it through maven use the following command.

> *mvn clean install -DskipTests exec:java*

Make sure you have java development kit version 21 installed before
running the maven command.

### 3.2 Docker

Just run the following command.

> *docker compose up --build*

### 3.3 How to test the api
The api could be tested through swagger ui.

>URL = http://localhost:8080/swagger-ui/index.html 

To login use the login endpoint *user/login* and copy the token created.
After that at the start of the page there is an authorize button. Click that
and paste the token into the field. There are 2 users that have been created for testing purposes.

User 1 asmith with roles USER and ADMIN

>email: *alice@example.com*  
>password: *pass*  

USER 2 jdoe with role ADMIN  

>email: *john@example.com*  
>password: *pass*

After this you can test the api.

## 4 Api-overview and endpoints
 
### 4.1 User
4.1.1 
#### URL: @GetMapping"/api/user/get/list"; description: "List of all active users. No Parameter required; Available for role: ADMIN"
4.1.2
#### URL: @GetMapping"/api/user/id/{id}"; description: "Get user by id. Valid@PathVariable("id") required; Available for role: USER and ADMIN"
4.1.3
#### URL: @GetMapping"/api/user/email/{email}"; description: "Get user by email. Available for role: ADMIN; Valid@PathVariable("email") required;"
4.1.4
#### URL: @PostMapping("/api/user/create"); description: Create new user with default 'USER' role and send back created UserResponseDto. If email is taken throws exception404/400/403. NO ROLE required;
4.1.5
#### URL:  @PutMapping("/api/user/update/{email}"); description: Update your own data, throws exception 400 if you try update someone else's data. @Valid @RequestBody UserRequestUpdateDto required; Available for roles: USER or ADMIN
4.1.6
#### URL: @PostMapping("/api/user/promoteUserToAdmin/{email}"); description: Promote OTHER user's 'USER' role to 'ADMIN'. If you try to promote yourself it throws Conflict-409 exception. Available for role: ADMIN; Valid@PathVariable("email") required;"
4.1.7
#### URL: @DeleteMapping("v/{email}"); description: Delete user by email. If you try to remove yourself from db it throws exception 409. Available for role: ADMIN; Valid@PathVariable("email") required;"
4.1.8
#### URL: @PostMapping("/api/user/login"); description: Create token for authentication to log in. NO ROLE required; Valid@@RequestParam(Strign: email&password) required;"
4.1.9
#### URL: @PostMapping("/api/user/updateRoles/{email}"); description: Update only OTHER's ROLES. Rewrite omeone's roles with new List<roles>. Available for role: ADMIN; Valid@PathVariable("email") & Valid@RequestBody Set<String> roles are required;"
 
### 4.2 Film

A API for displaying and creating films

to run locally use http://localhost:8080/<ENDPOINT>

#### 4.2.1 STATUS
Similarly to a heartbeat it checks that the API is live
URL: `/api/film/status`
Returns: "OK"    
Example:
>>/api/film/status
>
>>OK


#### 4.2.2 ALL
Returns a list of all films
The films within the list includs all avaiable fields
URL: `/api/film/all`  
Example:
>>/api/film/all
>
>>[{"id":1,"releaseYear":1941,"title":"Citizen Kane","genre":null,"casta":null,"ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"1.37:1","color":"Black-and-white","camera":null},
>>{"id":2,"releaseYear":1968,"title":"2001: A Space Odyssey","genre":null,"casta":null,"ageRestriction":11,"awards":null,"languages":null,"aspectRatio":"22:10","color":null,"camera":null}]


#### 4.2.3 ID
Returns one film with matching **id**, the film has all avaiable fields  
URL: `/api/film/id/<filmId>`
filmId:  the unique **ID** of a film  
Example:
>>/api/film/id/1
>
>>{"id":1,"releaseYear":1941,"title":"Citizen Kane","genre":null,"casta":null,"ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"1.37:1","color":"Black-and-white","camera":null}

#### 4.2.4 FIND BY TITLE
Returns one film matching the **title** compleatly
URL: `/api/film/title/<title>`
title: the film title  
Example:
>>/api/film/title/Inception 
>
>>{"id":3,"releaseYear":2010,"title":"Inception","genre":null,"casta":null,"ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"71:31","color":"color","camera":null}

#### 4.2.5 SEARCH BY TITLE
Returns all films with partial **title** match 
URL: `/api/film/search/<title>`
title: the film title  
Example:
>>/api/film/search/Citizen
>
>>[{"id":1,"releaseYear":1941,"title":"Citizen Kane","genre":null,"casta":null,"ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"1.37:1","color":"Black-and-white","camera":null}, ...]

#### 4.2.6 GENRE
Returns all films matching **genre**
URL: `/api/film/genre/<genre>`
genre: any film genre  
Example:
>>/api/film/genre/drama
>
>>[{"id":1,"releaseYear":1941,"title":"Citizen Kane","genre":"drama","casta":null,"ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"1.37:1","color":"Black-and-white","camera":null}, ... ]

#### 4.2.6 ACTOR
Returns all films that have **actor** in the casta (sic) field
URL: `/api/film/actor/<actor>`
actor: the name of a cast member  
Example:
>>/api/film/actor/Welles
>
>>[{"id":1,"releaseYear":1941,"title":"Citizen Kane","genre":"Drama","casta":"Orson Welles, Dorothy Comingore","ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"1.37:1","color":"Black-and-white","camera":null}, ... ]


#### 4.2.7 CREATE
Takes a JSON input of a film and returns it again (with an id) after saving it to the database
URL: `/api/film/create`  
Example:
>>/api/film/create
>
>>{"title" : "Inception", "releaseYear":2010, "genre":"Adventure"}
>
>>```json
>>{"id":3,"title" : "Inception", "releaseYear":2010, "genre":"Adventure"}
>>```

### 4.3 Review

Api for displaying, creating and editing reviews. Make sure to use the
*Bearer-token* when trying to access the protected endpoints. Can be
accuired through the *user/login* endpoint.

####

To run locally use http://localhost:8080/ + < Endpoint-URL >

###

#### 4.3.1 Get all reviews(Get-mapping)

Return all the current reviews and status 200, or empty list and status  
200 if there are no reviews.

>Access: Public access

>URL: *api/review/public/getAllReviews*

Input: *No input*

###

#### 4.3.2 Get review by filmId(Get-Mapping)

Returns the reviews for the specified film and status 200 if successful, or  
exception with status 404 if the film is absent.

>Access: Public access

>URL: *api/review/public/getByFilm/* < filmId >

filmId: *The index of the film*

###

#### 4.3.3 get review by user-email(Get-Mapping)

Returns the reviews from the user with the specified email and status 200
if successful, or exception with status 404 if the user is not found.

>Access: When logged in as USER

>URL: *api/review/user/getByUser/* < email >

email: *The email of the author of the review*

###

#### 4.3.4 Create a new review(Post-Mapping)

Creates a new Review and returns the review with status 200 if successful,  
or exception with status 404 when specified film is absent.

>Access: When logged in as USER

>URL: *api/review/user/createReview*

Input: *JSON Request body with format described below*

Type description:

>filmId : *Int*  
score : *Int*  
text : *String*

Example:

```json
{"filmId": 1, "score": 7, "text": "The film was good"}
```

###


#### 4.3.5 Update an existing review(Patch-Mapping)

Updates an existing review with the specified index and status 200
if the update was successful, exception 404 if the review is not found,
or exception 400 if the user tries to update a review not authored by
the user sending the request.

>Access: When logged in as USER

>URL: api/review/user/updateReview

Input: *JSON Request body with format described below*

Type description:

>reviewIndex : *Int*  
score : *Int*  
text : *String*

Example:

```json
{"reviewIndex": 1, "score": 5, "text": "changed my mind"}
```

###

#### 4.3.6 Delete a specific review by logged in user(Delete-Mapping)

Deletes the specified review and status 200 if the review
exists and are authored by the user sending the request,
exception 404 if the review is not found, or exception 400 if
the user tries to delete a review not authored by the user
sending the request.

>Access: When logged in as USER

>URL: *api/review/user/deleteReview/* < index >

index: *The index of the review to be deleted.*

###

#### 4.3.7 Delete any review (Delete-Mapping)

Deletes the specified review and status 200 if the review exists, or exception
with status 404 if the review does not exist.

>Access: When logged in as ADMIN

>URL: *api/review/admin/deleteReview/* < index >

index: *The index of the review to be deleted.*

###

#### 4.3.8 Get a specific review by review index(Get-Mapping)

Returns the review with the specified index and status 200 if successful, or
exception 404 if the review was not found.

>Access: When logged in as USER

>URL: *api/review/user/getSpecificReview/* < index >

index: *Index of the review to get.*


## 5 Known limitations
No known limitations in the functionality. When deploying the application,
database scaling needs to be done manually in the current version. The
application has been tested with java jdk 21 and mysql 8.0 and 8.4, so
cannot vouch for the application working with anything else at the moment
of writing.

## 6 Contributors

LaBeka : Addition Master, so many new options.

AnthonyRG : Front-end Master, good when you really only done backend
and embedded seriously at least.

DKWA0000 : "PS not so coherent, but decent implementations :p".