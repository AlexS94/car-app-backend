package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.entities.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    UserRepository userRepository;

    public Optional<User> getUser(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> validate(String input, String password) {
        Optional<User> user = userRepository.findByUserName(input);
        if (user.isEmpty()) {
            user = userRepository.findByEmail(input);
        }
        return user.isPresent() && user.get().getPassword().equals(password) ?
                user : Optional.empty();
    }

    public User addUser(User user) {
        if (userExists(user)) {
            throw new UserAlreadyExistsException("User already exists");
        }
        return userRepository.save(user);
    }

    public Optional<User> updateUser(User user) {
        return userExists(user) ?
                Optional.of(userRepository.save(user)) : Optional.empty();
    }

    private boolean userExists(User user) {
        return userRepository.existsUserByUserNameOrEmail(
                user.getUserName(), user.getEmail()
        );
    }

}
