package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import de.fakultaet73.galvanize.carapp.api.carappapi.entities.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
public class UserController {

    UserService userService;

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        Optional<User> optionalUser = userService.getUser(id);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/validate")
    public ResponseEntity<User> validateUser(@RequestParam String input, @RequestParam String password) {
        Optional<User> optionalUser = userService.validate(input, password);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        Optional<User> optionalUser = userService.addUser(user);
        return ResponseEntity.ok(optionalUser.get());
        //return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }







}
