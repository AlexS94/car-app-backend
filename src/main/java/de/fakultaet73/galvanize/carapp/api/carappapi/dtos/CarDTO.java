package de.fakultaet73.galvanize.carapp.api.carappapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.CarDetails;
import de.fakultaet73.galvanize.carapp.api.carappapi.Rating;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import lombok.*;
import org.bson.types.Binary;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CarDTO {

    private long id;

    private Long hostUserId;
    private String make;
    private String model;
    private String type;
    private Integer year;
    private Address address;

    private String[] features;
    private String description;
    private String[] guidelines;

    private Integer pricePerDay;
    private Integer distancePerDay;

    private CarDetails details;
    private List<Binary> images;
    private List<Rating> ratings;

    private List<Booking> bookings;

}
