# Car App Backend

This project is the backend-api to our car app cnd project.

Our webapp is a site to rent cars from private people, comparable to AirBnB.

Every user can host and rent cars here.

***

## Start the App

To start the app run:

### `./gradlew bootRun'` or `gradle bootRun` (gradle@7 required)

Start the app with fresh test data:

### `./gradlew bootRun --args='--loadData'` or

### `gradle bootRun --args='--loadData'`

Open [http://localhost:8080](http://localhost:8080) to view it in the browser.

***

## Notes

This app contains all of the endpoints needed for the:

`car-app`(work in progress)

***

## API Endpoints

### User endpoint

- **GET** (user with specific ID): `/user/{id}`
    - expected: *(REQUIRES valid ID)*
    - response: *User* | *404*
- **GET** (return user when input (username or email) and password exists): `/validate?input=INPUT&password=PASSWORD`
    - expected: *(REQUIRES two String for Username/Email and Password)*
    - response: *User* | *404*
- **POST** (add user): `/user`
    - expected *(REQUIRES valid user(without ID))*
    - response: *User*
- **PUT** (update user): `/user`
    - expected *(REQUIRES valid user)*
    - response: *User* | *404*
    - annotation: ***cannot update ID, username or email.***
- **PUT** (change password): `/user/password`
    - expected *(REQUIRES valid passwordContext)*
    - response: *200* | *404*
    - annotation: ***old and new password can't be identical.***
- **DELETE** (delete user with specific ID): `/user/{id}`
    - expected: *(REQUIRES valid ID)*
    - response: *200* | *204*

### Car Endpoint

- **GET** (car with specific ID): `/car/{id}`
    - expected: *(REQUIRES valid ID)*
    - response: *Car* | *404*
- **GET** (all cars): `/cars`
    - response: *List of all cars*
- **GET** (all cars with hostUserId): `/cars/host/{id}`
    - expected: *(REQUIRES valid hostUserId)*
    - response: *List of all cars with hostUserId*
- **GET** (car by city): `/cars/city/{city}`
    - expected: *(REQUIRES String city)*
    - response: *List of all Cars with city*
- **POST** (add car): `/car`
    - expected *(REQUIRES valid car(without ID))*
    - response: *Car* | *404*
- **PUT** (update car): `/car`
    - expected *(REQUIRES valid car)*
    - response: *Car* | *404*
    - annotation: ***update car -> ID cannot be changed***
- **DELETE** (delete car with specific ID): `/car/{id}`
    - expected: *(REQUIRES valid ID)*
    - response: *200* | *204*

### Image Endpoint

- **POST** (add image): `/file/image/`
    - expected: *(REQUIRES valid ID long AND String ReferenceType(CAR|USER),File JPEG)*
    - response: *200* | *500*
- **DELETE** (delete image): `/file/image/id`
    - expected *(REQUIRES valid ID)*
    - response: *ID* | *404*

### Booking Endpoint

- **POST** (add booking): `/booking`
    - expected: *(REQUIRES valid Booking (without ID))*
    - response: *200* | *404*
- **PUT** (update booking): `/booking`
    - expected: *(REQUIRES valid Booking (without ID))*
    - response: *200* | *404*
- **DELETE** (delete booking): `/booking/{id}`
    - expected: *(REQUIRES valid ID)*
    - response: *200* | *204*

### Valid Models

**Booking**:
```
  {
    userId(long),
    carId(long), 
    from(YYYY-MM-DD), 
    until(YYYY-MM-DD)
  }
```
**User**:
```
  {
    firstName(String),
    lastName(String),
    userName(String),
    email(String),
    password(String),
    dateOfBirth(YYYY-MM-DD),
    Address(Address),
    ratings([Rating])
  }
```
**ImageFile**:
```
  {
    referenceId(Long),
    referenceType(String),  ("USER" || "CAR")
    file(MutliPartFile)
  }
```
**Car**:
```
  {
    hostUserId(long),
    make(String),
    model(String),
    type(String),
    year(int),
    address(Address),
    pricePerDay(int),
    details(CarDetails),
    ratings([Rating])
    distancePerDay(int)     (not required)
    features(String[])      (not required)
    description(String)     (not required)
    guidelines(String[])    (not required)
  }
```
**CarDetails**:
```
  {
    fuelType(String),
    seats(int),
    doors(int),
    hp(int),
    transmission(String)  
  }
```
**Address**:
```
  {
    street(String),
    number(String),
    city(String),
    zip(int)
  }
```
**Rating**:
```
  {
    author(String),
    rating(double),
    date(YYYY-MM-DD),
    text(String)
  }
```
**PasswordContext**:
```
  {
    id(long),
    password(String)
  }
```

***

### Database MongoDB

- Documents:
    - Booking
    - Car
    - ImageFile
    - User
    - DatabaseSequence

***

## Our team

Marian Kowall, Alex Schulz, David Druecke, Marcel Westphal
