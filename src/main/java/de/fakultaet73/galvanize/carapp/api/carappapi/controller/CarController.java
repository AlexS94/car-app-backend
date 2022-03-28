package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.dtos.CarDTO;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.HostNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.BookingService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.CarService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
public class CarController {

    CarService carService;
    BookingService bookingService;
    ModelMapper modelMapper;

    @GetMapping("/car/{id}")
    public ResponseEntity<CarDTO> getCar(@PathVariable long id) {
        Optional<Car> optionalCar = carService.getCar(id);
        return optionalCar.map(
                body -> ResponseEntity.ok(convertToDTO(body)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/cars/host/{id}")
    public List<Car> getCarsByHostUserId(@PathVariable long id) {
        return carService.getCarsByHostUserId(id);
    }

    @GetMapping("/cars/city/{city}")
    public List<Car> getCarsByCity(@PathVariable String city) {
        return carService.getCarsByCity(city);
    }

    @PostMapping("/car")
    public Car addCar(@Valid @RequestBody Car car) {
        return carService.addCar(car);
    }

    @PutMapping("/car")
    public ResponseEntity<Car> updateCar(@Valid @RequestBody Car car) {
        Optional<Car> optionalCar = carService.updateCar(car);
        return optionalCar.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<Car> deleteCar(@PathVariable long id) {
        return carService.deleteCar(id) ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void HostNotExistsExceptionHandler(HostNotExistsException exception) {
    }

    private CarDTO convertToDTO(Car car) {
        CarDTO carDTO = modelMapper.map(car, CarDTO.class);
        carDTO.setBookings(bookingService.getBookingsByCarId(car.getId()));
        return carDTO;
    }

}
