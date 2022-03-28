package de.fakultaet73.galvanize.carapp.api.carappapi.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.CarDetails;
import de.fakultaet73.galvanize.carapp.api.carappapi.Rating;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarDTOUnitTest {

    ModelMapper modelMapper = new ModelMapper();
    ObjectMapper mapper = new ObjectMapper();

    Car carValid;
    CarDTO carTDOValid;

    @BeforeEach
    void setUp() {
        Long hostUserId = 3L;
        CarDetails carDetails = new CarDetails("diesel", 4, 5, 115, "manual");
        String[] features = {"smokefree", "petsfree"};
        String[] guidelines = {"parked behind the red building", "please return after use"};
        List<Rating> ratingsList = new ArrayList<>(List.of(
                new Rating("Dudebro", 2.8, LocalDate.now(), "Car was meh")));
        Address address = new Address("Musterstreet", "12", "Berlin", 12345);

        carValid = Car.builder()
                .hostUserId(hostUserId)
                .make("Volkswagen")
                .model("Golf")
                .type("Hatchback")
                .year(2013)
                .details(carDetails)
                .features(features)
                .description("clean, good condition")
                .guidelines(guidelines)
                .ratings(ratingsList)
                .pricePerDay(15)
                .distancePerDay(200)
                .address(address)
                .build();

        carTDOValid  = CarDTO.builder()
                .hostUserId(1L)
                .make("Volkswagen")
                .model("Golf")
                .type("Hatchback")
                .year(2013)
                .details(new CarDetails("diesel", 4, 5, 115, "manual"))
                .pricePerDay(15)
                .address(new Address("Musterstreet", "12", "Berlin", 12345))
                .bookings(  List.of(
                        new Booking(1, 2L, 1L, LocalDate.of(2022, 2, 1), LocalDate.of(2022, 2, 5)),
                        new Booking(2, 4L, 1L, LocalDate.of(2022, 3, 3), LocalDate.of(2022, 3, 12)),
                        new Booking(3, 6L, 1L, LocalDate.of(2022, 4, 1), LocalDate.of(2022, 4, 15))
                ))
                .build();
    }

    @Test
    void convertCarToCarDTO_returnsCarDTO() throws Exception {
        // Arrange
        // Act
        CarDTO carDTO = modelMapper.map(carValid, CarDTO.class);

        // Assert
        assertEquals(carValid.getId(), carDTO.getId());
        assertEquals(carValid.getHostUserId(), carDTO.getHostUserId());
        assertEquals(carValid.getMake(), carDTO.getMake());
        assertEquals(carValid.getModel(), carDTO.getModel());
        assertEquals(carValid.getType(), carDTO.getType());
        assertEquals(carValid.getYear(), carDTO.getYear());
        assertEquals(Arrays.toString(carValid.getFeatures()), Arrays.toString(carDTO.getFeatures()));
    /*    assertEquals(car.getDescription(), carDTO.getDescription());
        assertEquals(car.getGuidelines(), carDTO.getGuidelines());
        assertEquals(car.getRatings(), carDTO.getRatings());
        assertEquals(car.getPricePerDay(), carDTO.getPricePerDay());
        assertEquals(car.getDistancePerDay(), carDTO.getDistancePerDay() );
        assertEquals(car.getAddress(), carDTO.getAddress() );*/
        assertNull(carDTO.getBookings());

    }

    @Test
    public void whenConvertCarDTOToCarEntity_thenCorrect() {

        Car car = modelMapper.map(carTDOValid, Car.class);
        System.out.println(carTDOValid.toString());
        System.out.println(car.toString());
        assertEquals(carTDOValid.getId(), car.getId());
        assertEquals(carTDOValid.getHostUserId(), car.getHostUserId());
    }
}
