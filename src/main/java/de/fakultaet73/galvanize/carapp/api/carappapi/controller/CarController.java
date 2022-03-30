package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import de.fakultaet73.galvanize.carapp.api.carappapi.ReferenceType;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.dtos.CarDTO;
import de.fakultaet73.galvanize.carapp.api.carappapi.dtos.ImageFileDTO;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.BookingService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.CarService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.ImageFileService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class CarController {

    CarService carService;
    BookingService bookingService;
    ImageFileService imageFileService;
    ModelMapper modelMapper;

    @GetMapping("/car/{id}")
    public ResponseEntity<CarDTO> getCar(@PathVariable long id) {
        Optional<Car> optionalCar = carService.getCar(id);
        return optionalCar.map(
                        body -> ResponseEntity.ok(convertToDTO(body)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cars")
    public List<CarDTO> getAllCars() {
        return carService.getAllCars().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/cars/host/{id}")
    public List<CarDTO> getCarsByHostUserId(@PathVariable long id) {
        return carService.getCarsByHostUserId(id).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/cars/city/{city}")
    public List<CarDTO> getCarsByCity(@PathVariable String city) {
        return carService.getCarsByCity(city).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @PostMapping("/car")
    public CarDTO addCar(@Valid @RequestBody Car car) {
        return convertToDTO(carService.addCar(car));
    }

    @PutMapping("/car")
    public ResponseEntity<CarDTO> updateCar(@Valid @RequestBody CarDTO carDTO) throws Exception {
        Optional<Car> optionalCar = carService.updateCar(convertToDocument(carDTO));
        return optionalCar.map(
                        body -> ResponseEntity.ok(convertToDTO(body)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<Car> deleteCar(@PathVariable long id) {
        return carService.deleteCar(id) ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void UserNotExistsExceptionHandler(UserNotExistsException exception) {
    }

    private CarDTO convertToDTO(Car car) {
        CarDTO carDTO = modelMapper.map(car, CarDTO.class);
        carDTO.setBookings(bookingService.getBookingsByCarId(car.getId()));
        carDTO.setImages(
                imageFileService.getImageFiles(car.getId(), ReferenceType.CAR)
                        .stream().map(image -> modelMapper.map(image, ImageFileDTO.class))
                        .collect(Collectors.toList())
        );
        return carDTO;
    }

    private Car convertToDocument(CarDTO carDTO) throws Exception {
        return modelMapper.map(carDTO, Car.class);
    }

}
