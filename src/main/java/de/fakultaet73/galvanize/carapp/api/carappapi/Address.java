package de.fakultaet73.galvanize.carapp.api.carappapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class Address {

    @NotNull
    private String street;
    @NotNull
    private Integer number;
    @NotNull
    private String city;
    @NotNull
    private Integer zip;

}
