package de.fakultaet73.galvanize.carapp.api.carappapi.documents;

import de.fakultaet73.galvanize.carapp.api.carappapi.model.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.model.CarDetails;
import de.fakultaet73.galvanize.carapp.api.carappapi.model.Rating;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Car {

    @Transient
    public static final String SEQUENCE_NAME = "cars_sequence";

    @Id
    private long id;

    @NotNull
    private Long hostUserId;
    @NotNull @NotEmpty
    private String make;
    @NotNull @NotEmpty
    private String model;
    @NotNull @NotEmpty
    private String type;
    @NotNull
    private Integer year;
    @NotNull @Valid
    private Address address;

    private String[] features;
    private String description;
    private String[] guidelines;

    @NotNull
    private Integer pricePerDay;
    private Integer distancePerDay;

    @NotNull @Valid
    private CarDetails details;
    @Valid
    private List<Rating> ratings;

}
