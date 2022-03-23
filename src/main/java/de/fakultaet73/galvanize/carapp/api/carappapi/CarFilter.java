package de.fakultaet73.galvanize.carapp.api.carappapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class CarFilter {

    @NotNull
    private String make;

    @Override
    public String toString() {
        return "CarFilter{" +
                "make='" + make + '\'' +
                '}';
    }
}
