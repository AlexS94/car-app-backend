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
- GET: (return user when input (username or email) and password exists) `/validate?input=INPUT&password=PASSWORD`
- POST (add user): `/user` (REQUIRES valid user object in HTTP body (without ID)) 
- PATCH (add book to cart): `http://localhost:8082/api/books/cart/add/:id`

update user -> cant update username or email. THose 2 values cant be changed

***

## Our team

Marian Kowall, Alex Schulz, David Druecke, Marcel Westphal
