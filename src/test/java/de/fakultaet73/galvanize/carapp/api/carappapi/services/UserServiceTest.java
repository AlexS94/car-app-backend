package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.ReferenceType;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserAlreadyExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    CarService carService;

    @Mock
    BookingService bookingService;

    @Mock
    ImageFileService imageFileService;

    @Mock
    SequenceGeneratorService sequenceGeneratorService;

    User validUser;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, carService, bookingService, imageFileService, sequenceGeneratorService);
        validUser = User.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .userName("MrMax")
                .email("mustermann@web.de")
                .password("password")
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .address(new Address("Examplestreet", "12", "Berlin", 12345))
                .build();
    }

    @Test
    void userExists_id_returnsTrue() {
        // Arrange
        when(userRepository.existsUserById(anyLong())).thenReturn(true);

        // Act
        boolean result = userService.userExists(1L);

        // Assert
        assertTrue(result);
    }


    @Test
    void userExists_id_returnsFalse() {
        // Arrange
        when(userRepository.existsUserById(anyLong())).thenReturn(false);

        // Act
        boolean result = userService.userExists(1L);

        // Assert
        assertFalse(result);
    }

    @Test
    void getUser_id_returnsUserOptional() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(validUser));

        // Act
        Optional<User> result = userService.getUser(1);

        // Assert
        assertEquals(Optional.of(validUser), result);
    }

    @Test
    void getUser_id_notExists_returnsEmptyOptional() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

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
        when(userRepository.existsUserByUserNameOrEmail(anyString(), anyString()))
                .thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        // Act
        User result = userService.addUser(validUser);

        // Assert
        assertEquals(validUser, result);
    }

    @Test
    void addUser_user_alreadyExists_throwsUserAlreadyExistsException() {
        // Arrange
        when(userRepository.existsUserByUserNameOrEmail(anyString(), anyString()))
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
        when(userRepository.existsUserByIdAndUserNameAndEmail(anyLong(), anyString(), anyString()))
                .thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        // Act
        Optional<User> result = userService.updateUser(validUser);

        // Assert
        assertEquals(Optional.of(validUser), result);
    }

    @Test
    void updateUser_user_notExists_returnsNotFound() {
        // Arrange
        when(userRepository.existsUserByIdAndUserNameAndEmail(anyLong(), anyString(), anyString()))
                .thenReturn(false);
        // Act
        Optional<User> result = userService.updateUser(validUser);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteUser_id_returnsTrue() {
        // Arrange
        when(userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(imageFileService).deleteAllWithReferenceIdAndType(anyLong(), any(ReferenceType.class));
        doNothing().when(carService).deleteAllWithHostUserId(anyLong());
        doNothing().when(userRepository).deleteById(anyLong());

        // Act
        boolean result = userService.deleteUser(validUser.getId());

        // Assert
        assertTrue(result);
        verify(userRepository).deleteById(anyLong());
        verify(imageFileService).deleteAllWithReferenceIdAndType(anyLong(), any(ReferenceType.class));
        verify(carService).deleteAllWithHostUserId(anyLong());
    }

    @Test
    void deleteUser_id_notExists_returnsFalse() {
        // Arrange
        when(userRepository.existsById(anyLong())).thenReturn(false);
        // Act
        boolean result = userService.deleteUser(validUser.getId());

        // Assert
        assertFalse(result);
    }

}