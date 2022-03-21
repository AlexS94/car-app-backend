package de.fakultaet73.galvanize.carapp.api.carappapi.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.Rating;
import lombok.*;
import org.bson.types.Binary;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User {

    private int id;
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
