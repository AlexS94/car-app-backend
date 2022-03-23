# Car App API

This app contains all of the endpoints needed for the:

car-app-api

## NOTES
--

## API Endpoints

### User Endpoint
- GET: `/user/{id}` (get user by ID)
- GET: `/validate?input=INPUT&password=PASSWORD` (return user when input (username or email) and password exists) 
- POST: `/user` (add user) (REQUIRES valid user object in HTTP body (ID gets generated in db)) 
- PUT `/user` (update user) (EMAIL or USERNAME cannot be updated)
- DELETE `/User/{id}` (delete user by ID)
