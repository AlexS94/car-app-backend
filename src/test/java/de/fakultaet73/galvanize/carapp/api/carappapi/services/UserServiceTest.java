package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.entities.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    UserService userService;

    @Mock
    UserRepository userRepository;

    User validUser;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        validUser = User.builder()
                .id(1)
                .firstName("Max")
                .lastName("Mustermann")
                .userName("MrMax")
                .email("mustermann@web.de")
                .password("password")
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .address(new Address("Examplestreet", 12, "Berlin", 12345))
                .build();
    }

    @Test
    void getUser_id_returnsUserOptional() {
        // Arrange
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(validUser));

        // Act
        Optional<User> result = userService.getUser(1);

        // Assert
        assertEquals(Optional.of(validUser), result);
    }

    @Test
    void getUser_id_notExists_returnsEmptyOptional() {
        // Arrange
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUser(1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void validate_name_password_exists_returnsUserOptional() {
        // Arrange
        String input = "MrMax";
        String password = "password";
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(validUser));

        // Act
        Optional<User> result = userService.validate(input, password);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(input, result.get().getUserName());
        assertEquals(password, result.get().getPassword());
    }

    @Test
    void validate_name_password_notExists_returnsEmptyOptional() {
        // Arrange
        String input = "MrMax";
        String password = "password";
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.validate(input, password);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void validate_email_password_exists_returnsUserOptional() {
        // Arrange
        String input = "mustermann@web.de";
        String password = "password";
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(validUser));

        // Act
        Optional<User> result = userService.validate(input, password);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(input, result.get().getEmail());
        assertEquals(password, result.get().getPassword());
    }

    @Test
    void validate_email_password_notExists_returnsEmptyOptional() {
        // Arrange
        String input = "mustermann@web.de";
        String password = "password";
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.validate(input, password);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void validate_name_password_existsWrongPassword_returnsEmptyOptional() {
        // Arrange
        String input = "MrMax";
        String password = "12345";
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(validUser));

        // Act
        Optional<User> result = userService.validate(input, password);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void validate_email_password_existsWrongPassword_returnsEmptyOptional() {
        // Arrange
        String input = "mustermann@web.de";
        String password = "12345";
        when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(validUser));

        // Act
        Optional<User> result = userService.validate(input, password);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void addUser_user_returnsUser() {
        // Arrange
        when(userRepository.findByUserNameOrEmailExists(anyString(), anyString()))
                .thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        // Act
        User result = userService.addUser(validUser);

        // Assert
        assertEquals(validUser, result);
    }

    @Test
    void addUser_user_alreadyExists_returnsConflict() {
        // Arrange
        when(userRepository.findByUserNameOrEmailExists(anyString(), anyString()))
                .thenReturn(true);

        // Act // Assert
        assertThrows(UserAlreadyExistsException.class,
                () -> userService.addUser(validUser),
                "Exception was expected"
        );
    }

    @Test
    void updateUser_user_returnsUser() {
        // Arrange
        when(userRepository.findByUserNameOrEmailExists(anyString(), anyString()))
                .thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        // Act
        Optional<User>result = userService.updateUser(validUser);

        // Assert
        assertEquals(Optional.of(validUser), result);
    }

    @Test
    void updateUser_user_notExists_returnsNotFound() {
        // Arrange
        when(userRepository.findByUserNameOrEmailExists(anyString(), anyString()))
                .thenReturn(false);
        // Act
        Optional<User>result = userService.updateUser(validUser);

        // Assert
        assertTrue(result.isEmpty());
    }

}