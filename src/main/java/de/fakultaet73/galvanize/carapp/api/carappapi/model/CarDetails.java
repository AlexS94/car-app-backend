package de.fakultaet73.galvanize.carapp.api.carappapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class CarDetails {

    @NotNull @NotEmpty
    private String fuelType;
    @NotNull
    private Integer seats;
    @NotNull
    private Integer doors;
    @NotNull
    private Integer hp;
    @NotNull @NotEmpty
    private String transmission;

}
