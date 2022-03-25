package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserAlreadyExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    CarService carService;
    BookingService bookingService;
    SequenceGeneratorService sequenceGeneratorService;

    public UserService(UserRepository userRepository,@Lazy CarService carService, @Lazy BookingService bookingService,
                       SequenceGeneratorService sequenceGeneratorService) {
        this.userRepository = userRepository;
        this.carService = carService;
        this.bookingService = bookingService;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public boolean userExists(long id) {
        return userRepository.existsById(id);
    }

    public Optional<User> getUser(long id) {
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
        user.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
        return userRepository.save(user);
    }

    public Optional<User> updateUser(User user) {
        return userExists(user) ?
                Optional.of(userRepository.save(user)) : Optional.empty();
    }

    public boolean deleteUser(long id) {
        if (userRepository.existsById(id)) {
            bookingService.deleteAllWithUserId(id);
            carService.deleteAllWithHostUserId(id);
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private boolean userExists(User user) {
        return userRepository.existsUserByUserNameOrEmail(
                user.getUserName(), user.getEmail()
        );
    }

//    public void addCarIdToHostUser(long hostUserId, long carId) {
//        Optional<User> optionalUserToUpdate = userRepository.findById(hostUserId);
//        if (optionalUserToUpdate.isPresent()) {
//            User userToUpdate = optionalUserToUpdate.get();
//            userToUpdate.addCarToList(carId);
//            userRepository.save(userToUpdate);
//        }
//    }

//    public void deleteCarIdFromHostUser(long hostUserId, long carId) {
//        Optional<User> optionalUserToUpdate = userRepository.findById(hostUserId);
//        if (optionalUserToUpdate.isPresent()) {
//            User userToUpdate = optionalUserToUpdate.get();
//            userToUpdate.addCarToList(carId);
//            userRepository.save(userToUpdate);
//        }
//    }

}
