package de.fakultaet73.galvanize.carapp.api.carappapi.exceptions;

public class BookingAlreadyExistsException extends  RuntimeException {

    public BookingAlreadyExistsException(String message) {
        super(message);
    }
}
