package de.fakultaet73.galvanize.carapp.api.carappapi.exceptions;

public class UserNotExistsException extends RuntimeException{

    public UserNotExistsException(String message) {
        super(message);
    }
}
