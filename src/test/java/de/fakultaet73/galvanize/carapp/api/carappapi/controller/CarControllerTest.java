package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.CarDetails;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.dtos.CarDTO;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.HostNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.BookingService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.CarService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class CarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CarService carService;

    @MockBean
    UserService userService;

    @MockBean
    BookingService bookingService;

    @MockBean
    ModelMapper modelMapper;

    ObjectMapper mapper = new ObjectMapper();

    Car validCar;
    CarDTO validCarDTO;
    String validCarJson;
    String json;

    @BeforeEach
    void setUp() throws Exception {

        validCar = Car.builder()
                .hostUserId(1L)
                .make("Volkswagen")
                .model("Golf")
                .type("Hatchback")
                .year(2013)
                .details(new CarDetails("diesel", 4, 5, 115, "manual"))
                .pricePerDay(15)
                .address(new Address("Musterstreet", "12", "Berlin", 12345))
                .build();
        json = mapper.writeValueAsString(validCar);
        validCarJson = "{\"id\":0,\"hostUserId\":1,\"make\":\"Volkswagen\",\"model\":\"Golf\",\"type\":\"Hatchback\",\"year\":2013,\"address\":{\"street\":\"Musterstreet\",\"number\":12,\"city\":\"Berlin\",\"zip\":12345},\"features\":null,\"description\":null,\"guidelines\":null,\"pricePerDay\":15,\"distancePerDay\":null,\"details\":{\"fuelType\":\"diesel\",\"seats\":4,\"doors\":5,\"hp\":115,\"transmission\":\"manual\"},\"images\":null,\"ratings\":null,\"bookings\":null}";

        validCarDTO = CarDTO.builder()
                .hostUserId(1L)
                .make("Volkswagen")
                .model("Golf")
                .type("Hatchback")
                .year(2013)
                .details(new CarDetails("diesel", 4, 5, 115, "manual"))
                .pricePerDay(15)
                .address(new Address("Musterstreet", "12", "Berlin", 12345))
                .build();
    }

    @Test
    void getCar_id_returnsCar() throws Exception {
        // Arrange
        when(carService.getCar(anyLong())).thenReturn(Optional.of(validCar));
        when(modelMapper.map(any(Car.class), any()))
                .thenReturn(validCarDTO);
       when(bookingService.getBookingsByCarId(anyLong())).thenReturn(
                List.of(
                        new Booking(1, 2L, 1L, LocalDate.of(2022, 2, 1), LocalDate.of(2022, 2, 5)),
                        new Booking(2, 4L, 1L, LocalDate.of(2022, 3, 3), LocalDate.of(2022, 3, 12)),
                        new Booking(3, 6L, 1L, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 4, 15))
                )
        );

        // Act
        mockMvc.perform(get("/car/1"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void getCar_id_notFound_returnsNotFound() throws Exception {
        // Arrange
        when(carService.getCar(anyLong())).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(get("/car/1"))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    void getCar_id_wrongFormat_returnsBadRequest() throws Exception {
        // Arrange
        when(carService.getCar(anyLong())).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(get("/car/aa"))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCars_returnsCars() throws Exception {
        // Arrange
        List<Car> carList = new ArrayList<>();
        carList.add(validCar);
        String jsonList = mapper.writeValueAsString(carList);

        when(carService.getAllCars()).thenReturn(carList);
        when(modelMapper.map(any(Car.class), any()))
                .thenReturn(validCarDTO);

        // Act
        mockMvc.perform(get("/cars"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList)).andDo(print());
    }

    @Test
    void getCarsByHostUserId_hostUserId_returnsCars() throws Exception {
        // Arrange
        List<Car> carList = new ArrayList<>();
        carList.add(validCar);
        carList.add(validCar);
        carList.add(validCar);
        String jsonList = mapper.writeValueAsString(carList);

        when(carService.getCarsByHostUserId(anyLong())).thenReturn(carList);
        when(modelMapper.map(any(Car.class), any()))
                .thenReturn(validCarDTO);

        // Act
        mockMvc.perform(get("/cars/host/1"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList));
    }

    @Test
    void getCarsByHostUserId_hostUserId_returnsEmptyList() throws Exception {
        // Arrange
        List<Car> carList = new ArrayList<>();
        when(carService.getCarsByHostUserId(anyLong())).thenReturn(carList);
        when(modelMapper.map(any(Car.class), any()))
                .thenReturn(validCarDTO);
        // Act
        mockMvc.perform(get("/cars/host/99"))
                // Assert
                .andExpect(status().isOk());
    }

    @Test
    void getCarsByHostUserId_hostUserId_returnsBadRequest() throws Exception {
        // Act
        mockMvc.perform(get("/cars/host/aa"))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCarsByCity_city_returnsCars() throws Exception {
        //Arrange
        List<Car> carList = new ArrayList<>();
        carList.add(validCar);
        carList.add(validCar);
        carList.add(validCar);
        String jsonList = mapper.writeValueAsString(carList);
        when(carService.getCarsByCity(anyString())).thenReturn(carList);
        when(modelMapper.map(any(Car.class), any()))
                .thenReturn(validCarDTO);
        //Act
        mockMvc.perform(get("/cars/city/wolfsburg"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList));
    }

    @Test
    void getCarsByCity_city_returnsEmptyList() throws Exception {
        //Arrange
        List<Car> carList = new ArrayList<>();
        String jsonList = mapper.writeValueAsString(carList);
        when(carService.getCarsByCity(anyString())).thenReturn(carList);
        when(modelMapper.map(any(Car.class), any()))
                .thenReturn(validCarDTO);
        //Act
        mockMvc.perform(get("/cars/city/wolfsburg"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList))
                .andDo(print());
    }

    @Test
    void addCar_Car_returnsCar() throws Exception {
        // Arrange
        when(carService.addCar(any(Car.class))).thenReturn(validCar);
        when(modelMapper.map(any(Car.class), any()))
                .thenReturn(validCarDTO);
        System.out.println(json);
        // Act
        mockMvc.perform(post("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void addCar_emptyObject_returnsBadRequest() throws Exception {
        // Arrange
        json = "{}";

        // Act
        mockMvc.perform(post("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void addCar_Car_missingParam_returnsBadRequest() throws Exception {
        // Act
        Car invalidCar = Car.builder()
                .hostUserId(1L)
                .make("Volkswagen")
                .type("Hatchback")
                .year(2013)
                .details(new CarDetails("diesel", 4, 5, 115, "manual"))
                .pricePerDay(15)
                .address(new Address("Musterstreet", "12", "Berlin", 12345))
                .build();
        json = mapper.writeValueAsString(invalidCar);

        mockMvc.perform(post("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void addCar_Car_validAddress_returnsCar() throws Exception {
        // Act
        when(carService.addCar(any(Car.class))).thenReturn(validCar);
        when(modelMapper.map(any(Car.class), any()))
                .thenReturn(validCarDTO);
        mockMvc.perform(post("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void addCar_Car_invalidAddress_returnsBadRequest() throws Exception {
        // Act
        String json = "{\"id\":0,\"hostUserId\":1,\"make\":\"Volkswagen\",\"model\":\"Golf\",\"type\":\"Hatchback\",\"year\":2013,\"address\":{\"street\":\"Musterstreet\",\"city\":\"Berlin\",\"zip\":12345},\"features\":null,\"description\":null,\"guidelines\":null,\"pricePerDay\":15,\"distancePerDay\":null,\"details\":{\"fuelType\":\"diesel\",\"seats\":4,\"doors\":5,\"hp\":115,\"transmission\":\"manual\"},\"images\":null,\"ratings\":null,\"bookings\":null}";

        mockMvc.perform(post("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void addCar_Car_invalidDetails_returnsBadRequest() throws Exception {
        // Act
        String json = "{\"id\":0,\"hostUserId\":1,\"make\":\"Volkswagen\",\"model\":\"Golf\",\"type\":\"Hatchback\",\"year\":2013,\"address\":{\"street\":\"Musterstreet\",\"number\":12,\"city\":\"Berlin\",\"zip\":12345},\"features\":null,\"description\":null,\"guidelines\":null,\"pricePerDay\":15,\"distancePerDay\":null,\"details\":{\"fuelType\":\"diesel\",\"doors\":5,\"hp\":115,\"transmission\":\"manual\"},\"images\":null,\"ratings\":null,\"bookings\":null}";
        mockMvc.perform(post("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void addCar_Car_hostUserIdNotExists_returnsBadRequest() throws Exception {
        // Arrange
        when(carService.addCar(any(Car.class))).thenThrow(HostNotExistsException.class);
        // Act
        mockMvc.perform(post("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCar_Car_returnsCar() throws Exception {
        // Arrange
        when(modelMapper.map(any(CarDTO.class), any()))
                .thenReturn(validCar);
        when(modelMapper.map(any(Car.class), any()))
                .thenReturn(validCarDTO);
        when(carService.updateCar(any(Car.class))).thenReturn(Optional.of(validCar));
        // Act
        mockMvc.perform(put("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void updateCar_emptyObject_returnsBadRequest() throws Exception {
        // Arrange
        json = "{}";

        // Act
        mockMvc.perform(put("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCar_carNotExists_or_hostUserIdDeviating_returnsNotFound() throws Exception {
        // Arrange
        when(carService.updateCar(any(Car.class))).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(put("/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Assert
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCar_id_returnsOk() throws Exception {
        when(carService.deleteCar(anyLong())).thenReturn(true);

        // Act
        mockMvc.perform(delete("/car/1"))
                // Assert
                .andExpect(status().isOk());
    }

    @Test
    void deleteCar_id_notFound_returnsNoContent() throws Exception {
        when(carService.deleteCar(anyLong())).thenReturn(false);

        // Act
        mockMvc.perform(delete("/car/1"))
                // Assert
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCar_noParam_returnsMethodNotAllowed() throws Exception {
        // Act
        mockMvc.perform(delete("/car"))
                // Assert
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void deleteCar_wrongFormat_returnsBadRequest() throws Exception {
        // Act
        mockMvc.perform(delete("/car/aa"))
                // Assert
                .andExpect(status().isBadRequest());
    }


}
