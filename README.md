# Car App Backend

This project is the backend-api to our car app cnd project.

Our webapp is a site to rent cars from private people, comparable to AirBnB.

Every user can host and rent cars here.

***
## Start the App

To start the app run:

### `start app command here`
### `npm start`

Open [http://localhost:8080](http://localhost:8080) to view it in the browser.

***

## Notes

This app contains all of the endpoints needed for the:

car-app (work in progress)

***

## API Endpoints

### User Endpoint
- GET: (user with specific ID) `/user/{id}`  
  - expected: *(REQUIRES valid id long)*
  - response: *Right(HttpStatus.ok(User))* | *Wrong(HttpStatus.NOT_FOUND)*
- GET: (return user when input (username or email) and password exists) `/validate?input=INPUT&password=PASSWORD`
  - expected: *(REQUIRES two String for Username/Email and Password)*
  - response: *Right(HttpStatus.ok(User))* | *Wrong(HttpStatus.NOT_FOUND)*
- POST (add user): `/user` 
  - expected *(REQUIRES valid user(without ID))*
  - response: *User*
- PUT (update user): `http://localhost:8082/api/books/cart/add/:id`
  - expected *(REQUIRES valid user)* 
  - response: *Right(HttpStatus.ok(User))* | *Wrong(HttpStatus.NOT_FOUND)*
  - annotation: ***update user -> cant update  id, username or email. THose 3 values cant be changed***
- Delete: (Delete user with specific ID) `/user/{id}`
  - expected: *(REQUIRES valid id long)*
  - response: *Right(HttpStatus.ok(id))* |  *Wrong(HttpStatus.NO_CONTENT)*



### Car Endpoint
- **GET:** (Car with specific ID) `/car/{id}`
  - expected: *(REQUIRES valid id long)*
  - response: *Right(HttpStatus.ok(Car))* | *Wrong(HttpStatus.NOT_FOUND)*
- **GET:** (All Cars) `/cars`   
  - expected: *--*
  - response: *List from Cars with Cars or Empty*
- **GET:** (All Cars with hostUserId) `/cars/host/{id}`
  - expected: *(REQUIRES valid hostUserId long)*
  - response: *List from Cars with Cars or Empty*
- **GET:** (Car by City) `/cars/city/{city}`
   - expected: *(REQUIRES one String for city)*
   - response: *List from Cars with Cars or Empty*
- **Post:** (Add Car) `/cars`
   - expected *(REQUIRES valid car(without ID))*
   - response: *Right(HttpStatus.ok(Car))* | *Wrong(HttpStatus.NOT_FOUND)*
- **Put:**(Update Car) `/cars`
   - expected *(REQUIRES valid car)*
   - response: *Right(HttpStatus.ok(Car))* | *Wrong(HttpStatus.NOT_FOUND)*
   - annotation: ***update car -> id cannot be change***
- **Delete:**(Delete Car with specified id) `/car/{id}`
    - expected: *(REQUIRES valid id long)*
    - response: *Right(HttpStatus.ok(Car))* | *Wrong(HttpStatus.NO_CONTENT)*

### Image Endpoint
- **Post:** (Add Image) `/file/image/`
   - expected: *(REQUIRES valid id long AND String ReferenceType(CAR|USER),File JPEG)*
   - response: Right(HttpStatus.ok("image upload success")) |  Wrong(HttpStatus.INTERNAL_SERVER_ERROR("image upload failed"))
- **Delete:**(Delete Image) `/file/image/id`
  - expected *(REQUIRES valid id long)*
  - response: *Right(HttpStatus.ok(id))* | *Wrong(HttpStatus.NOT_FOUND)*

### Booking Endpoint
- **Post:** (Add Booking) `/booking`
  - expected: *(REQUIRES valid Booking (without ID))*
  - response: *Right(HttpStatus.ok())* | *Wrong(HttpStatus.NOT_FOUND)*
- **Put:** (Update Booking) `/booking` 
  - expected: *(REQUIRES valid Booking (without ID))*
  - response: *Right(HttpStatus.ok())* | *Wrong(HttpStatus.NOT_FOUND)*
- **Delete:** (Delete Booking) `/booking/{id}`
  - expected: *(REQUIRES valid id long)*
  - response: *Right(HttpStatus.ok())* | *Wrong(HttpStatus.NO_CONTENT)*
  
### Valid Models
- **Booking** : userId(long), carId(long), from(LocalDate), until(localDate)
- **User** : firstname(String), lastName(String), username(String), email(String), password(String), dateOfbirth(localDate), valid address, valid list of Ratings
- **ImageFile**: referenceId(Long), ReferenceType(String), createDate(LocalDate, content(Binary))
- **Car** : hostUserId(long), make(String), model(String),type(String),year(Integer), valid address, pricePerDay(Integer), valid CarDetails, valid list of Ratings
- **CarDetails** : fuelType(String), seats(Integer), doors(Integer),hp(Integer),transmission(String),
- **address:** : street(String), number(String), city(String), zip(integer)
- **Rating** : author(String), rating(double), date(LocalDate, test(String))

***
### Database MongoDB 
- Documents: 
  - Booking 
  - Car
  - ImageFile
  - DatabaseSequence
  - User
***

## Our team

Marian Kowall, Alex Schulz, David Druecke, Marcel Westphal
