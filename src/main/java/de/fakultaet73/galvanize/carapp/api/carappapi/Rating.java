package de.fakultaet73.galvanize.carapp.api.carappapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Getter
public class Rating {

    private final String author;
    private final double rating;
    private final LocalDate date;
    private final String text;

}
