package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.enums.ReferenceType;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.CarRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    CarRepository carRepository;
    UserService userService;
    BookingService bookingService;
    ImageFileService imageFileService;
    SequenceGeneratorService sequenceGeneratorService;

    public CarService(CarRepository carRepository,
                      @Lazy UserService userService,
                      @Lazy BookingService bookingService,
                      @Lazy ImageFileService imageFileService,
                      SequenceGeneratorService sequenceGeneratorService) {
        this.carRepository = carRepository;
        this.userService = userService;
        this.bookingService = bookingService;
        this.imageFileService = imageFileService;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public Optional<Car> getCar(long id) {
        return carRepository.findById(id);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getCarsByCity(String city) {
        return carRepository.findByAddressContains(city);
    }

    public List<Car> getCarsByHostUserId(long id) {
        return carRepository.findByHostUserId(id);
    }

    public Car addCar(Car car) {
        if (!userService.userExists(car.getHostUserId())) {
            throw new UserNotExistsException("hostUserId does not exist");
        }
        car.setId(sequenceGeneratorService.generateSequence(Car.SEQUENCE_NAME));
        return carRepository.save(car);
    }

    public Optional<Car> updateCar(Car car) {
        return carExistsWithIdAndHostUserId(car) ?
                Optional.of(carRepository.save(car)) : Optional.empty();
    }

    public boolean deleteCar(long id) {
        if (carRepository.existsById(id)) {
            bookingService.deleteAllWithCarId(id);
            imageFileService.deleteAllWithReferenceIdAndType(id, ReferenceType.CAR);
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAllWithHostUserId(long hostUserId) {
        List<Car> carsToDelete = carRepository.findAllByHostUserId(hostUserId);
        carsToDelete.forEach(car -> deleteCar(car.getId()));
    }

    private boolean carExistsWithIdAndHostUserId(Car car) {
        return carRepository.existsCarByIdAndHostUserId(car.getId(), car.getHostUserId());
    }

    public boolean carExists(long id) {
        return carRepository.existsCarById(id);
    }
}
