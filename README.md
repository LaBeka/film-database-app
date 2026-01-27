# film-database-app
 
## 1 Project-description:
"A Spring Boot backend application designed to manage a movie database, user interactions, and a review system. It features secure Role-Based Access Control to ensure appropriate permissions for public users, authenticated members, and administrators. API Documentation: Fully integrated with Swagger/OpenAPI for easy endpoint testing and exploration".
 
## 2 Technology used
The project uses java running springboot and have implemented a REST-API
The project can be run locally but intends to be running in a docker compose in the cloud
## 3 How to run project (local and docker)
## 4 Api-overview and endpoints
 
### 4.1 User
4.1.1 
URL: @GetMapping"/api/user/get/list"; description: "List of all active users. No Parameter required; Available for role: ADMIN"
4.1.2 
URL: @GetMapping"/api/user/id/{id}"; description: "Get user by id. Valid@PathVariable("id") required; Available for role: USER and ADMIN"
4.1.3 
URL: @GetMapping"/api/user/email/{email}"; description: "Get user by email. Available for role: ADMIN; Valid@PathVariable("email") required;"
4.1.4 
URL: @PostMapping("/api/user/create"); description: Create new user with default 'USER' role and send back created UserResponseDto. If email is taken throws exception404/400/403. NO ROLE required;
4.1.5 
URL:  @PutMapping("/update/{email}"); description: Update your own data, throws exception 400 if you try update someone else's data. @Valid @RequestBody UserRequestUpdateDto required; Available for roles: USER or ADMIN
4.1.6 
URL: @PostMapping("/promoteUserToAdmin/{email}"); description: Promote OTHER user's 'USER' role to 'ADMIN'. If you try to promote yourself it throws Conflict-409 exception. Available for role: ADMIN; Valid@PathVariable("email") required;"
4.1.7 
URL: @DeleteMapping("/{email}"); description: Delete user by email. If you try to remove yourself from db it throws exception 409. Available for role: ADMIN; Valid@PathVariable("email") required;"
4.1.8 
URL: @PostMapping("/login"); description: Create token for authentication to log in. NO ROLE required; Valid@@RequestParam(Strign: email&password) required;"
4.1.9 
URL: @PostMapping("/updateRoles/{email}"); description: Update only OTHER's ROLES. Rewrite omeone's roles with new List<roles>. Available for role: ADMIN; Valid@PathVariable("email") & Valid@RequestBody Set<String> roles are required;"
 
### 4.2 Film

"/api/film

STATUS
URL: `/api/film/status`
Returns URL: "OK" 
Similarly to a heartbeat it checks that the API is live
ex.
>>/api/film/status
>
>>OK



ALL
URL: `/api/film/all`
Returns a list of all films
The films within the list includs all avaiable fields
>>/api/film/all
>
>>[{"id":1,"releaseYear":1941,"title":"Citizen Kane","genre":null,"casta":null,"ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"1.37:1","color":"Black-and-white","camera":null},
>>{"id":2,"releaseYear":1968,"title":"2001: A Space Odyssey","genre":null,"casta":null,"ageRestriction":11,"awards":null,"languages":null,"aspectRatio":"22:10","color":null,"camera":null}]


ID
URL: `/api/film/id/{id}`
Returns one film with matching **id**, the film has all avaiable fields
>>/api/film/id/1
>
>>{"id":1,"releaseYear":1941,"title":"Citizen Kane","genre":null,"casta":null,"ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"1.37:1","color":"Black-and-white","camera":null}

FIND BY TITLE
URL: `/api/film/title/{title}`
Returns one film matching the title compleatly
>>/api/film/title/Inception 
>
>>{"id":3,"releaseYear":2010,"title":"Inception","genre":null,"casta":null,"ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"71:31","color":"color","camera":null}

SEARCH BY TITLE
URL: `/api/film/search/{title}`
Returns all films with partial title match 
>>/api/film/search/Citizen
>
>>{"id":1,"releaseYear":1941,"title":"Citizen Kane","genre":null,"casta":null,"ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"1.37:1","color":"Black-and-white","camera":null}

GENRE
URL: `/api/film/genre/{genre}`
Returns all films matching **genre**
>>/api/film/genre/Drama
>
>>[{"id":1,"releaseYear":1941,"title":"Citizen Kane","genre":"Drama","casta":null,"ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"1.37:1","color":"Black-and-white","camera":null}, ... ]

ACTOR
URL: `/api/film/actor/{actor}`
Returns all films that have **actor** in the casta (sic) field
>>/api/film/actor/Welles
>
>>[{"id":1,"releaseYear":1941,"title":"Citizen Kane","genre":"Drama","casta":"Orson Welles, Dorothy Comingore","ageRestriction":15,"awards":null,"languages":null,"aspectRatio":"1.37:1","color":"Black-and-white","camera":null}, ... ]


CREATE
URL: `/api/film/create`
Takes a JSON input of a film and returns it again (with an id) after saving it to the database
>>/api/film/create
>
>>{"title" : "Inception", "releaseYear":2010, "genre":"Adventure"}
>
>>{"id":3,"title" : "Inception", "releaseYear":2010, "genre":"Adventure"}

### 4.3 Review
## 5 Known limitations
