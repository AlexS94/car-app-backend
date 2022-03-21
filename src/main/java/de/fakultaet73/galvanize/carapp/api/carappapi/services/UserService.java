package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.entities.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    public Optional<User> getUser(int id) {
        return null;
    }

    public Optional<User> validate(String input, String password) {
        return null;
    }

    public User addUser(User user) {
        return null;
    }

    public Optional<User> updateUser(User user) {
        return null;
    }

    private boolean verifyUser(User user) {
        return false;
    }

}
