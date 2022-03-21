package de.fakultaet73.galvanize.carapp.api.carappapi.entities;

import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.Rating;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void create_new_user() {
        // Arrange
        Address address = new Address("Musterstreet", 12, "Berlin", 12345);
        List<Rating> ratings = new ArrayList<>(List.of(new Rating("Dudebro", 2.8, LocalDate.now(), "Car was meh")));
        LocalDate date = LocalDate.of(1994, 3, 1);

        User user = User.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .userName("MrMax")
                .email("max@test.de")
                .password("12356")
                .dateOfBirth(date)
                .address(address)
                .ratings(ratings)
                .build();
        // Assert
        assertEquals("Max", user.getFirstName());
        assertEquals("Mustermann", user.getLastName());
        assertEquals("MrMax", user.getUserName());
        assertEquals("max@test.de", user.getEmail());
        assertEquals("12356", user.getPassword());
        assertEquals(date, user.getDateOfBirth());
        assertEquals(ratings, user.getRatings());
        assertEquals(address, user.getAddress());
    }

}