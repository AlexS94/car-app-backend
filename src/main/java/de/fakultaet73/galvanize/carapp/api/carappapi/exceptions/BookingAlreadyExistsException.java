package de.fakultaet73.galvanize.carapp.api.carappapi.exceptions;


//ToDo: ich glaube die brauchen wir nicht.
public class BookingAlreadyExistsException extends  RuntimeException {

    public BookingAlreadyExistsException(String message) {
        super(message);
    }
}
