package de.fakultaet73.galvanize.carapp.api.carappapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class Address {

    @NotNull @NotEmpty
    private String street;
    @NotNull @NotEmpty
    private String number;
    @NotNull @NotEmpty
    private String city;
    @NotNull
    private Integer zip;

}
