# Car App API

This app contains all of the endpoints needed for the:

car-app-api

## NOTES
--

## API Endpoints

### User Endpoint
- GET: (user with specific ID) `/user/{id}`
- GET: (return user when input (username or email) and password exists) `/validate?input=INPUT&password=PASSWORD`
- POST (add user): `/user` (REQUIRES valid user object in HTTP body (without ID)) 
- PATCH (add book to cart): `http://localhost:8082/api/books/cart/add/:id`

update user -> cant update username or email. THose 2 values cant be changed



### API Unit - Bookstore
- GET (all books) / POST (add book): `http://localhost:8082/api/books`
- DELETE: `http://localhost:8082/api/books/:id`
- PATCH (add book to cart): `http://localhost:8082/api/books/cart/add/:id`
- PATCH (remove book from cart): `http://localhost:8082/api/books/cart/remove/:id`

### Project: React Inbox
- GET (all messages): `http://localhost:8082/api/messages`
- PATCH (updates multiple messages, see API for req.body requirements): `http://localhost:8082/api/messages`  
``` Example req.body to mark messages 1,2,3 as read
{
  "messageIds": [1,2,3],
  "command": "read",
  "read": true
}
```

### Redux Unit - Reddit Clone
- GET (all posts) /POST (add post): `http://localhost:8082/api/posts`
- DELETE: `http://localhost:8082/api/posts/:id`
- GET (up-vote): `http://localhost:8082/api/posts/votes/increase/:id`
- GET (down-vote): `http://localhost:8082/api/posts/votes/decrease/:id`

- GET (all comments) / POST (add comment): `http://localhost:8082/api/comments`
- DELETE: `http://localhost:8082/api/comments/:id`

### Router Unit - User Login
- GET (all users): `http://localhost:8082/api/users`
- POST (add user): `http://localhost:8082/api/users`
- POST (user login): `http://localhost:8082/api/login` (REQUIRES USER EMAIL AND PASSWORD)

