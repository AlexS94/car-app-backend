package de.fakultaet73.galvanize.carapp.api.carappapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Address {

    private final String street;
    private final int number;
    private final String City;
    private final int zip;
}
