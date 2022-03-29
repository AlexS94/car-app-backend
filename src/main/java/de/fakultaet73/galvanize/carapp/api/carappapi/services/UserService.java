package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.ReferenceType;
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
    ImageFileService imageFileService;
    SequenceGeneratorService sequenceGeneratorService;

    public UserService(UserRepository userRepository,
                       @Lazy CarService carService,
                       @Lazy BookingService bookingService,
                       @Lazy ImageFileService imageFileService,
                       SequenceGeneratorService sequenceGeneratorService) {
        this.userRepository = userRepository;
        this.carService = carService;
        this.bookingService = bookingService;
        this.imageFileService = imageFileService;
        this.sequenceGeneratorService = sequenceGeneratorService;
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
        if (userExistsByUserNameAndEmail(user)) {
            throw new UserAlreadyExistsException("User already exists");
        }
        user.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
        return userRepository.save(user);
    }

    public Optional<User> updateUser(User user) {
        return userExistsByUserNameAndEmail(user) ?
                Optional.of(userRepository.save(user)) : Optional.empty();
    }

    public boolean deleteUser(long id) {
        if (userRepository.existsById(id)) {
            bookingService.deleteAllWithUserId(id);
            carService.deleteAllWithHostUserId(id);
            imageFileService.deleteAllWithReferenceIdAndType(id, ReferenceType.USER);
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private boolean userExistsByUserNameAndEmail(User user) {
        return userRepository.existsUserByUserNameOrEmail(
                user.getUserName(), user.getEmail()
        );
    }

    public boolean userExists(Long userId) {
        return userRepository.existsUserById(userId);
    }
}



