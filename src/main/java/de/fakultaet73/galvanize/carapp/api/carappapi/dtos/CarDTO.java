package de.fakultaet73.galvanize.carapp.api.carappapi.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fakultaet73.galvanize.carapp.api.carappapi.model.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.model.CarDetails;
import de.fakultaet73.galvanize.carapp.api.carappapi.model.Rating;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CarDTO {

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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ImageFileDTO> images;

    @Valid
    private List<Rating> ratings;

    private List<Booking> bookings;

}
