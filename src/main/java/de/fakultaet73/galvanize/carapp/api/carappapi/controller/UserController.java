package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import de.fakultaet73.galvanize.carapp.api.carappapi.dtos.ImageFileDTO;
import de.fakultaet73.galvanize.carapp.api.carappapi.dtos.UserCarsDTO;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.dtos.UserDTO;
import de.fakultaet73.galvanize.carapp.api.carappapi.enums.ReferenceType;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserAlreadyExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.model.PasswordContext;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.BookingService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.CarService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.ImageFileService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class UserController {

    UserService userService;
    BookingService bookingService;
    CarService carService;
    ImageFileService imageFileService;
    ModelMapper modelMapper;

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable long id) {
        Optional<User> optionalUser = userService.getUser(id);
        return optionalUser.map(
                        body -> ResponseEntity.ok(convertToDTO(body)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/validate")
    public ResponseEntity<UserDTO> validateUser(@RequestParam String input, @RequestParam String password) {
        Optional<User> optionalUser = userService.validate(input, password);
        return optionalUser.map(
                        body -> ResponseEntity.ok(convertToDTO(body)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/user")
    public UserDTO addUser(@Valid @RequestBody User user) {
        return convertToDTO(userService.addUser(user));
    }

    @PutMapping("/user")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = userService.updateUser(convertToDocument(userDTO));
        return optionalUser.map(
                        body -> ResponseEntity.ok(convertToDTO(body)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/user/password")
    public ResponseEntity<String> updateUser(@Valid @RequestBody PasswordContext body) {
        return userService.changePassword(body.getId(), body.getPassword()) ?
                ResponseEntity.ok().build() :
                new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        return userService.deleteUser(id) ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public void UserAlreadyExistsExceptionHandler(UserAlreadyExistsException exception) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void UserNotExistsExceptionHandler(UserNotExistsException exception) {
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setBookings(bookingService.getBookingsByUserId(user.getId()));
        imageFileService.getImageFile(userDTO.getId(), ReferenceType.USER)
                .ifPresent(image -> {
                    userDTO.setImage(modelMapper.map(image, ImageFileDTO.class));
                });
        userDTO.setCars(
                carService.getCarsByHostUserId(user.getId())
                        .stream().map(car -> modelMapper.map(car, UserCarsDTO.class))
                        .collect(Collectors.toList()));
        return userDTO;
    }

    private User convertToDocument(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(userService.getPassword(user.getId()));
        return user;
    }

}
