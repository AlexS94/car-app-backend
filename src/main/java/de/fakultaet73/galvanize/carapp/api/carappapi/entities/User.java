package de.fakultaet73.galvanize.carapp.api.carappapi.entities;

import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User {

    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private Binary image;
    private Address address;
    private List<Rating> ratings;
    private int[] cars;
    private int[] bookedCars;

}
