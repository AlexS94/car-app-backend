package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.BookingAlreadyExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.InvalidBookingException;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class BookingControllerTest {

    @Autowired
    MockMvc mockMvc;


    @MockBean
    BookingService bookingService;

    @MockBean
    CarService carService;

    @MockBean
    UserService userService;

    @MockBean
    ImageFileService imageFileService;

    @MockBean
    ModelMapper modelMapper;

    ObjectMapper mapper = new ObjectMapper();

    Booking validBooking;
    String json;

    @BeforeEach
    void setUp() throws Exception {
        validBooking = Booking.builder()
                .userId(1L)
                .carId(2L)
                .from(LocalDate.of(2022, 2, 2))
                .until(LocalDate.of(2022, 3, 2))
                .build();
        json = mapper.writeValueAsString(validBooking);
    }

    @Test
    void addBooking_booking_returnsBooking() throws Exception {
        // Arrange
        when(bookingService.addBooking(any(Booking.class))).thenReturn(validBooking);
        // Act
        mockMvc.perform(post("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(json));

    }

    @Test
    void addBooking_Booking_alreadyExists_returnsConflict() throws Exception {
        // Arrange
        when(bookingService.addBooking(any(Booking.class))).thenThrow(BookingAlreadyExistsException.class);

        // Act
        mockMvc.perform(post("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isConflict());
    }

    @Test
    void addBooking_Booking_fromDateBeforeUntilDate_returnsBadRequest() throws Exception {
        // Arrange
        when(bookingService.addBooking(any(Booking.class))).thenThrow(InvalidBookingException.class);

        // Act
        mockMvc.perform(post("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBooking_emptyObject_returnsBadRequest() throws Exception {
        // Arrange
        json = "{}";

        // Act
        mockMvc.perform(post("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBooking_missingParam_returnsBadRequest() throws Exception {
        // Act
        Booking invalidBooking = Booking.builder()
                .userId(1L)
                .carId(2L)
                .until(LocalDate.of(2022, 3, 2))
                .build();
        json = mapper.writeValueAsString(invalidBooking);

        mockMvc.perform(post("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBooking_Booking_returnsBooking() throws Exception {
        // Arrange
        when(bookingService.updateBooking(any(Booking.class))).thenReturn(Optional.of(validBooking));

        // Act
        mockMvc.perform(put("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void updateBooking_Booking_BookingNotExists_returnsNotFound() throws Exception {
        // Arrange
        when(bookingService.updateBooking(any(Booking.class))).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(put("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBooking_emptyObject_returnsBadRequest() throws Exception {
        // Arrange
        json = "{}";

        // Act
        mockMvc.perform(put("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteBooking_id_returnsOk() throws Exception {
        when(bookingService.deleteBooking(anyLong())).thenReturn(true);

        // Act
        mockMvc.perform(delete("/booking/1"))
                // Assert
                .andExpect(status().isOk());
    }

    @Test
    void deleteBooking_id_notFound_returnsNoContent() throws Exception {
        when(bookingService.deleteBooking(anyLong())).thenReturn(false);

        // Act
        mockMvc.perform(delete("/booking/1"))
                // Assert
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBooking_noParam_returnsMethodNotAllowed() throws Exception {
        // Act
        mockMvc.perform(delete("/booking"))
                // Assert
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void deleteBooking_wrongFormat_returnsBadRequest() throws Exception {
        // Act
        mockMvc.perform(delete("/booking/aa"))
                // Assert
                .andExpect(status().isBadRequest());
    }

}
