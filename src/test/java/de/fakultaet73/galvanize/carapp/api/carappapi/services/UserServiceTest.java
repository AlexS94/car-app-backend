package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    UserService userService;

    @Mock
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        user = User.builder().userName("Max").lastName("Mustermann").build();
    }

    @Test
    void getUser_id_returnsOptionalUser() {
        // Arrange
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUser(1);

        // Assert
        assertEquals(Optional.of(user), result);
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

}