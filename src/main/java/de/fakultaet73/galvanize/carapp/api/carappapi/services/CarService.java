package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.HostNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CarService {

    CarRepository carRepository;
    UserService userService;
//    BookingService bookingService;
    SequenceGeneratorService sequenceGeneratorService;

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
            throw new HostNotExistsException("hostUserId does not exist");
        }
        car.setId(sequenceGeneratorService.generateSequence(Car.SEQUENCE_NAME));
        userService.addCarIdToHostUser(car.getHostUserId(), car.getId());
        return carRepository.save(car);
    }

    public Optional<Car> updateCar(Car car) {
        return carExists(car) ?
                Optional.of(carRepository.save(car)) : Optional.empty();
    }

    public boolean deleteCar(long id) {
        Optional<Car> car = getCar(id);
        if (car.isPresent()) {
           // userService.deleteCarIdFromHostUser(car.get().getHostUserId(), car.get().getId());
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private boolean carExists(Car car) {
        return carRepository.existsCarByIdAndHostUserId(car.getId(), car.getHostUserId());
    }

    public void deleteAllCarsWithHostUserId(long hostUserId) {
        carRepository.deleteAllByHostUserId(hostUserId);
    }

}
