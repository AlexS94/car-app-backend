package de.fakultaet73.galvanize.carapp.api.carappapi.documents;

import de.fakultaet73.galvanize.carapp.api.carappapi.Rating;
import de.fakultaet73.galvanize.carapp.api.carappapi.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.CarDetails;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarTest {

    @Test
    void create_new_car() {
        // Arrange
        Long hostUserId = 3L;
        CarDetails carDetails = new CarDetails("diesel", 4, 5, 115, "manual");
        String[] features = {"smokefree", "petsfree"};
        String[] guidelines = {"parked behind the red building", "please return after use"};
        List<Rating> ratingsList = new ArrayList<>(List.of(
                new Rating("Dudebro", 2.8, LocalDate.now(), "Car was meh")));
        Address address = new Address("Musterstreet", "12", "Berlin", 12345);

        Car car = Car.builder()
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
        // Assert
        assertEquals(hostUserId, car.getHostUserId());
        assertEquals("Volkswagen", car.getMake());
        assertEquals("Golf", car.getModel());
        assertEquals("Hatchback", car.getType());
        assertEquals(2013, car.getYear());
        assertEquals(carDetails, car.getDetails());
        assertEquals(features, car.getFeatures());
        assertEquals("clean, good condition", car.getDescription());
        assertEquals(guidelines, car.getGuidelines());
        assertEquals(ratingsList, car.getRatings());
        assertEquals(15, car.getPricePerDay());
        assertEquals(200, car.getDistancePerDay());
        assertEquals(address, car.getAddress());

    }

}
