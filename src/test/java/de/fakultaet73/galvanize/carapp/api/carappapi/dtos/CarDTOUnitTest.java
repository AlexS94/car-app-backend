package de.fakultaet73.galvanize.carapp.api.carappapi.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.CarDetails;
import de.fakultaet73.galvanize.carapp.api.carappapi.Rating;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
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

    @Test
    void convertCarToCarDTO_returnsCarDTO() throws Exception {
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

        // Act
        CarDTO carDTO = modelMapper.map(car, CarDTO.class);

        // Assert
        assertEquals(car.getId(), carDTO.getId());
        assertEquals(car.getHostUserId(), carDTO.getHostUserId());
        assertEquals(car.getMake(), carDTO.getMake());
        assertEquals(car.getModel(), carDTO.getModel());
        assertEquals(car.getType(), carDTO.getType());
        assertEquals(car.getYear(), carDTO.getYear());
        assertEquals(Arrays.toString(car.getFeatures()), Arrays.toString(carDTO.getFeatures()));
    /*    assertEquals(car.getDescription(), carDTO.getDescription());
        assertEquals(car.getGuidelines(), carDTO.getGuidelines());
        assertEquals(car.getRatings(), carDTO.getRatings());
        assertEquals(car.getPricePerDay(), carDTO.getPricePerDay());
        assertEquals(car.getDistancePerDay(), carDTO.getDistancePerDay() );
        assertEquals(car.getAddress(), carDTO.getAddress() );*/
        assertNull(carDTO.getBookings());

    }

   /* @Test
    public void whenConvertPostDtoToPostEntity_thenCorrect() {
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setTitle(randomAlphabetic(6));
        postDto.setUrl("www.test.com");

        Post post = modelMapper.map(postDto, Post.class);
        assertEquals(postDto.getId(), post.getId());
        assertEquals(postDto.getTitle(), post.getTitle());
        assertEquals(postDto.getUrl(), post.getUrl());
    }*/
}
