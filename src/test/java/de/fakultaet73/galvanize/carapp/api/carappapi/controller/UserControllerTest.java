package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fakultaet73.galvanize.carapp.api.carappapi.entities.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @Test
    void getUser_id_returnsUser() throws Exception {
        // Arrange
        User user = new User("Max", "Mustermann");
        String json = mapper.writeValueAsString(user);
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
        User user = new User("Max", "Mustermann");
        String json = mapper.writeValueAsString(user);

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
        User user = new User("Max", "Mustermann");
        String json = mapper.writeValueAsString(user);

        when(userService.addUser(any(User.class))).thenReturn(Optional.of(user));

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
    }

    @Test
    void addUser_User_wrongFormat_returnsBadRequest() throws Exception {
    }



    /*
        PATCH update
        POST create
     */

}
