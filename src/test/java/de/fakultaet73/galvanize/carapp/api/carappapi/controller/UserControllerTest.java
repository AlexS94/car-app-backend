package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fakultaet73.galvanize.carapp.api.carappapi.entities.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.InvalidUserException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserAlreadyExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    ObjectMapper mapper = new ObjectMapper();

    User user;
    String json;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        user = User.builder().userName("Max").lastName("Mustermann").build();
        json = mapper.writeValueAsString(this.user);
    }

    @Test
    void getUser_id_returnsUser() throws Exception {
        // Arrange
        when(userService.getUser(anyInt())).thenReturn(Optional.of(user));

        // Act
        mockMvc.perform(get("/user/1"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void getUser_id_notFound_returnsNotFound() throws Exception {
        // Arrange
        when(userService.getUser(anyInt())).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(get("/user/1"))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    void getUser_id_wrongFormat_returnsBadRequest() throws Exception {
        // Arrange
        when(userService.getUser(anyInt())).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(get("/user/aa"))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void validateUser_input_password_returnsUser() throws Exception {
        // Arrange
        when(userService.validate(anyString(), anyString())).thenReturn(Optional.of(user));

        // Act
        mockMvc.perform(get("/validate?input=max&password=test"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void validateUser_input_password_notFound_returnsNotFound() throws Exception {
        // Arrange
        when(userService.getUser(anyInt())).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(get("/validate?input=max&password=test"))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    void addUser_User_returnsUser() throws Exception {
        // Arrange
        when(userService.addUser(any(User.class))).thenReturn(user);

        // Act
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void addUser_User_alreadyExists_returnsConflict() throws Exception {
        // Arrange
        when(userService.addUser(any(User.class))).thenThrow(UserAlreadyExistsException.class);

        // Act
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isConflict());
    }

    @Test
    void addUser_emptyObject_returnsBadRequest() throws Exception {
        // Arrange
        json = "{}";
        when(userService.addUser(any(User.class))).thenThrow(InvalidUserException.class);

        // Act
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_User_returnsUser() throws Exception {
        // Arrange
        when(userService.updateUser(any(User.class))).thenReturn(Optional.of(user));

        // Act
        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void updateUser_User_UserNotExists_returnsNotFound() throws Exception {
        // Arrange
        when(userService.updateUser(any(User.class))).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_emptyObject_returnsBadRequest() throws Exception {
        // Arrange
        json = "{}";
        when(userService.updateUser(any(User.class))).thenThrow(InvalidUserException.class);

        // Act
        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

}
