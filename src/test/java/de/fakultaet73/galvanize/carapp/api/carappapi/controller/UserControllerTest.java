package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.dtos.UserCarsDTO;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.model.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.dtos.UserDTO;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserAlreadyExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.model.PasswordContext;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.BookingService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.CarService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.ImageFileService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
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

    @MockBean
    CarService carService;

    @MockBean
    BookingService bookingService;

    @MockBean
    ImageFileService imageFileService;

    @MockBean
    ModelMapper modelMapper;

    ObjectMapper mapper = new ObjectMapper();

    User validUser;
    UserDTO validUserDTO;
    String json;
    String jsonDTO;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        validUser = User.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .userName("Max")
                .email("mustermann@web.de")
                .password("password")
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .address(new Address("Examplestreet", "12", "Berlin", 12345))
                .build();

        validUserDTO = UserDTO.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .userName("Max")
                .email("mustermann@web.de")
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .address(new Address("Examplestreet", "12", "Berlin", 12345))
                .bookings(List.of(
                        new Booking(1, 2L, 1L, LocalDate.of(2022, 2, 1), LocalDate.of(2022, 2, 5)),
                        new Booking(2, 4L, 1L, LocalDate.of(2022, 3, 3), LocalDate.of(2022, 3, 12)),
                        new Booking(3, 6L, 1L, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 4, 15))
                ))
                .cars(List.of(UserCarsDTO.builder().build()))
                .build();


        json = mapper.writeValueAsString(this.validUser);
        jsonDTO = mapper.writeValueAsString(this.validUserDTO);
    }

    @Test
    void getUser_id_returnsUser() throws Exception {
        // Arrange
        when(userService.getUser(anyLong())).thenReturn(Optional.of(validUser));

        // convertToDTO mocks
        when(modelMapper.map(any(User.class), any())).thenReturn(validUserDTO);
        when(bookingService.getBookingsByUserId(anyLong())).thenReturn(validUserDTO.getBookings());
        when(carService.getCarsByHostUserId(anyLong())).thenReturn(List.of(Car.builder().build()));
        when(modelMapper.map(any(Car.class), any())).thenReturn(UserCarsDTO.builder().build());

        // Act
        mockMvc.perform(get("/user/1"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonDTO));
    }

    @Test
    void getUser_id_notFound_returnsNotFound() throws Exception {
        // Arrange
        when(userService.getUser(anyLong())).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(get("/user/1"))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    void getUser_id_wrongFormat_returnsBadRequest() throws Exception {
        // Arrange
        when(userService.getUser(anyLong())).thenReturn(Optional.empty());


        // Act
        mockMvc.perform(get("/user/aa"))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void validateUser_input_password_returnsUser() throws Exception {
        // Arrange
        when(userService.validate(anyString(), anyString())).thenReturn(Optional.of(validUser));

        // convertToDTO mocks
        when(modelMapper.map(any(User.class), any())).thenReturn(validUserDTO);
        when(bookingService.getBookingsByUserId(anyLong())).thenReturn(validUserDTO.getBookings());
        when(carService.getCarsByHostUserId(anyLong())).thenReturn(List.of(Car.builder().build()));
        when(modelMapper.map(any(Car.class), any())).thenReturn(UserCarsDTO.builder().build());

        // Act
        mockMvc.perform(get("/validate?input=max&password=test"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonDTO));
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
        when(userService.addUser(any(User.class))).thenReturn(validUser);

        // convertToDTO mocks
        when(modelMapper.map(any(User.class), any())).thenReturn(validUserDTO);
        when(bookingService.getBookingsByUserId(anyLong())).thenReturn(validUserDTO.getBookings());
        when(carService.getCarsByHostUserId(anyLong())).thenReturn(List.of(Car.builder().build()));
        when(modelMapper.map(any(Car.class), any())).thenReturn(UserCarsDTO.builder().build());

        // Act
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonDTO));
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

        // Act
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_missingParam_returnsBadRequest() throws Exception {
        // Act
        User invalidUser = User.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .email("mustermann.de")
                .password("password")
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .address(new Address("Examplestreet", "12", "Berlin", 12345))
                .build();
        json = mapper.writeValueAsString(invalidUser);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_validAddress_returnsUser() throws Exception {
        // Act
        when(userService.addUser(any(User.class))).thenReturn(validUser);

        // convertToDTO mocks
        when(modelMapper.map(any(User.class), any())).thenReturn(validUserDTO);
        when(bookingService.getBookingsByUserId(anyLong())).thenReturn(validUserDTO.getBookings());
        when(carService.getCarsByHostUserId(anyLong())).thenReturn(List.of(Car.builder().build()));
        when(modelMapper.map(any(Car.class), any())).thenReturn(UserCarsDTO.builder().build());

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonDTO));
    }

    @Test
    void addUser_invalidAddress_returnsBadRequest() throws Exception {
        // Act
        String json = "{\"id\":1,\"firstName\":\"Max\",\"lastName\":\"Mustermann\",\"userName\":\"Max\",\"email\":\"mustermann@web.de\",\"password\":\"password\",\"dateOfBirth\":\"1999-01-01\",\"image\":null,\"address\":{\"street\":\"Examplestreet\",\"number\":12,\"city\":\"Berlin\"},\"ratings\":null,\"cars\":null,\"bookedCars\":null}";

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_invalidEmail_returnsBadRequest() throws Exception {
        // Act
        User invalidUser = User.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .userName("Max")
                .email("mustermann.de")
                .password("password")
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .address(new Address("Examplestreet", "12", "Berlin", 12345))
                .build();
        json = mapper.writeValueAsString(invalidUser);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_User_returnsUser() throws Exception {
        // Arrange
        when(userService.updateUser(any(User.class))).thenReturn(Optional.of(validUser));

        // convertToDocument mocks
        when(modelMapper.map(any(UserDTO.class), any())).thenReturn(validUser);
        when(userService.getPassword(anyLong())).thenReturn("password");

        // convertToDTO mocks
        when(modelMapper.map(any(User.class), any())).thenReturn(validUserDTO);
        when(bookingService.getBookingsByUserId(anyLong())).thenReturn(validUserDTO.getBookings());
        when(carService.getCarsByHostUserId(anyLong())).thenReturn(List.of(Car.builder().build()));
        when(modelMapper.map(any(Car.class), any())).thenReturn(UserCarsDTO.builder().build());

        // Act
        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonDTO));
    }

    @Test
    void updateUser_User_UserNotExists_returnsNotFound() throws Exception {
        // Arrange
        when(userService.updateUser(any(User.class))).thenReturn(Optional.empty());

        // convertToDocument mocks
        when(modelMapper.map(any(UserDTO.class), any())).thenReturn(validUser);
        when(userService.getPassword(anyLong())).thenReturn("password");

        // convertToDTO mocks
        when(modelMapper.map(any(User.class), any())).thenReturn(validUserDTO);
        when(bookingService.getBookingsByUserId(anyLong())).thenReturn(validUserDTO.getBookings());
        when(carService.getCarsByHostUserId(anyLong())).thenReturn(List.of(Car.builder().build()));
        when(modelMapper.map(any(Car.class), any())).thenReturn(UserCarsDTO.builder().build());

        // Act
        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDTO))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_emptyObject_returnsBadRequest() throws Exception {
        // Arrange
        json = "{}";

        // Act
        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_id_returnsOk() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn(true);

        // Act
        mockMvc.perform(delete("/user/1"))
                // Assert
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_id_notFound_returnsNoContent() throws Exception {
        when(userService.deleteUser(anyLong())).thenReturn(false);

        // Act
        mockMvc.perform(delete("/user/1"))
                // Assert
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_noParam_returnsMethodNotAllowed() throws Exception {
        // Act
        mockMvc.perform(delete("/user"))
                // Assert
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void deleteUser_wrongFormat_returnsBadRequest() throws Exception {
        // Act
        mockMvc.perform(delete("/user/aa"))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void changePassword_id_password_returnsOk() throws Exception {
        // Arrange
        when(userService.changePassword(anyLong(), anyString())).thenReturn(true);
        PasswordContext passwordContext = new PasswordContext(1L, "newPassword");

        // Act
        mockMvc.perform(put("/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(passwordContext)))
        // Assert
                .andExpect(status().isOk());
    }


    @Test
    void changePassword_id_password_samePassword_returnsConflict() throws Exception {
        // Arrange
        when(userService.changePassword(anyLong(), anyString())).thenReturn(false);
        PasswordContext passwordContext = new PasswordContext(1L, "newPassword");

        // Act
        mockMvc.perform(put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passwordContext)))
                // Assert
                .andExpect(status().isConflict());
    }


    @Test
    void changePassword_id_password_idNotFound_returnsNotFound() throws Exception {
        // Arrange
        when(userService.changePassword(anyLong(), anyString())).thenThrow(UserNotExistsException.class);
        PasswordContext passwordContext = new PasswordContext(1L, "newPassword");
        // Act
        mockMvc.perform(put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(passwordContext)))
                // Assert
                .andExpect(status().isNotFound());
    }

}
