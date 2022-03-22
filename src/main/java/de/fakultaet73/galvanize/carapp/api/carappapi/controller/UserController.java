package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserAlreadyExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@AllArgsConstructor
@RestController
public class UserController {

    UserService userService;

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        Optional<User> optionalUser = userService.getUser(id);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/validate")
    public ResponseEntity<User> validateUser(@RequestParam String input, @RequestParam String password) {
        Optional<User> optionalUser = userService.validate(input, password);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/user")
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/user")
    public ResponseEntity<User>updateUser(@Valid @RequestBody User user) {
        Optional<User> optionalUser = userService.updateUser(user);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public void UserAlreadyExistsExceptionHandler(UserAlreadyExistsException exception) {
    }

}
