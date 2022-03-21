package de.fakultaet73.galvanize.carapp.api.carappapi.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
