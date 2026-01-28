# film-database-app
 
## 1 Project-description:
"A Spring Boot backend application designed to manage a movie database, user interactions, and a review system. It features secure Role-Based Access Control to ensure appropriate permissions for public users, authenticated members, and administrators. API Documentation: Fully integrated with Swagger/OpenAPI for easy endpoint testing and exploration".
 
## 2 Technology used
The project uses java running springboot and have implemented a REST-API
The project can be run locally but intends to be running in a docker compose in the cloud
## 3 How to run project (local and docker)
## 4 Api-overview and endpoints
 
### 4.1 User
###
#### 4.1.1 List of all active users(get-mapping)
Return list of all active users and status 200, or empty list and status.

>Access: ADMIN access
> 
>URL: *api/user/get/list*
> 
Input: *No input*

#### 4.1.2 Get user by id(get-mapping)
Return selected user by id and return status 200. If Not found return status 404.

>Access: USER and ADMIN access
> 
>URL: */api/user/id/{id}*
> 
Input: *@Valid @PathVariable("userId")*

#### 4.1.3 Get user by email(get-mapping)
Return selected user by email and status 200, if not found return status 404.

>Access: ADMIN access
> 
>URL: */api/user/email/{email}*
>
Input: *@Valid @PathVariable("email")*


#### 4.1.4 Create new user(post-mapping)
Create new user with default USER_ROLE and return created user with status 200. If email is taken stop creating new user and return code 400 with its message

>Access: public access
>
>URL: */api/user/create*
>
Input: *@Valid @RequestBody UserRequestDto*


#### 4.1.5 Update user's data(put-mapping)
Update your own data(all properties except for email, roles and password) and return updated user with status 200. 
If emails of authenticated user and user who is getting updated do not match return status 400.

>Access: USER or ADMIN access
>
>URL: */api/user/update/{email}*
>
Input: *@Valid @RequestBody UserRequestUpdateDto*

#### 4.1.6 Promote ROLES (post-mapping)
Promote OTHER user's 'USER' role to 'ADMIN' and return promoted user with status 200.
If you try to promote your own role it returns status 404. 

>Access: ADMIN access
>
>URL: */api/user/promoteUserToAdmin/{email}*
>
Input: *@Valid @PathVariable("email")*

#### 4.1.7 Delete user by email(delete-mapping)
Soft deactivation of user by email and return deactivated user with status 200.
If you try to deactivate yourself it returns status 409.

>Access: ADMIN access
>
>URL: */api/user/{email}*
>
Input: *@Valid @PathVariable("email")*


#### 4.1.8 Generate JWT-token(post-mapping)
Generate token for authentication to log in and return token with code 200.
If password and email not found or not match returns status 404.

>Access: PUBLIC access
>
>URL: *api/user/login*
>
Input: *@Valid @RequestParam(String: email & password)*


#### 4.1.9 Update roles(post-mapping)
Rewrite someone's roles with new List<Roles>  and return promoted user with status 200.
If you try to promote your own role it returns status 404.

>Access: ADMIN access
>
>URL: */api/user/updateRoles/{email}*
>
Input: *@Valid @PathVariable("email") @RequestBody Set<String> *

 
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

Api for displaying and editing reviews.
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

Input: *JSON with format described below*

Type description:

>reviewIndex : *Int*  
score : *Int*  
text : *String*

Example:

```json
{"reviewIndex": 1, "score": 7, "text": "The film was good"}
```

###


#### 4.3.5 Update an existing review(Patch-Mapping)

Updates an existing review with the specified index and status 200
if the update was successful, exception 404 if the review is not found,
or exception 400 if the user tries to update a review not authored by
the user sending the request.

>Access: When logged in as USER

>URL: api/review/user/updateReview

Input: *JSON with format described below*

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
