package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.CarDetails;
import de.fakultaet73.galvanize.carapp.api.carappapi.CarFilter;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.CarService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    ObjectMapper mapper = new ObjectMapper();

    Car validCar;
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
                .address(new Address("Musterstreet", 12, "Berlin", 12345))
                .build();
        json = mapper.writeValueAsString(validCar);
    }

    @Test
    void getCar_id_returnsCar() throws Exception {
        // Arrange
        when(carService.getCar(anyLong())).thenReturn(Optional.of(validCar));

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

    //ToDO
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

        // Act
        mockMvc.perform(get("/cars"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList)).andDo(print());
    }

    @Test
    void getCars_hostUserId_returnsCars() throws Exception {
        // Arrange
        List<Car> carList = new ArrayList<>();
        carList.add(validCar);
        carList.add(validCar);
        carList.add(validCar);
        String jsonList = mapper.writeValueAsString(carList);

        when(carService.getCars(anyLong())).thenReturn(carList);

        // Act
        mockMvc.perform(get("/cars/1"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList));
    }

    @Test
    void getCars_hostUserId_returnsEmptyList() throws Exception {
        // Arrange
        List<Car> carList = new ArrayList<>();
        when(carService.getCars(anyLong())).thenReturn(carList);

        // Act
        mockMvc.perform(get("/cars/99"))
                // Assert
                .andExpect(status().isOk());
    }

    @Test
    void getCars_hostUserId_returnsBadRequest() throws Exception {
        // Arrange
        List<Car> carList = new ArrayList<>();
        when(carService.getCars(anyLong())).thenReturn(carList);

        // Act
        mockMvc.perform(get("/cars/aa"))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFilteredCars_Makes_returnsCars() throws Exception {
        //Arrange
        List<Car> carList = new ArrayList<>();
        carList.add(validCar);
        carList.add(validCar);
        carList.add(validCar);
        String jsonList = mapper.writeValueAsString(carList);
        when(carService.getFilteredCars(any(CarFilter.class))).thenReturn(carList);
        //Act
        mockMvc.perform(get("/cars/filter?make=volkswagen"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList));
    }

    @Test
    void getFilteredCars_simpleFilters_returnsCars() throws Exception {
        //Arrange
        List<Car> carList = new ArrayList<>();
        carList.add(validCar);
        carList.add(validCar);
        carList.add(validCar);
        String jsonList = mapper.writeValueAsString(carList);
        when(carService.getFilteredCars(any(CarFilter.class))).thenReturn(carList);
        //Act
        mockMvc.perform(get("/cars/" +
                        "filter?make=volkswagen&model=golf&type=sov&features=smokeFree,petsFree&" +
                        "pricePerDay=10&details:{fuelType,seats,doors,hp,transmission&" +
                        "rating=2.5}"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList));
    }




    /*
Filter
Fuel
Cartype
Seats
Transmission
------------
Doors
Makes
(Model)
(Features)
*/

    // Ein Auto nach Id
    // Alle Autos
    // Alle Autos die zu eine Hostid gehören
    // Alle Autos die zu einer City gehören
    // Alle Autos die zu einer Zip gehören
    // Alle Autos die zu Marke
    // Alle Autos die zu einen Typ gehören
    //


}
