package de.fakultaet73.galvanize.carapp.api.carappapi.controller;


import de.fakultaet73.galvanize.carapp.api.carappapi.CarFilter;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.CarService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@RestController
public class CarController {

    CarService carService;

    @GetMapping("/car/{id}")
    public ResponseEntity<Car> getCar(@PathVariable long id) {
       Optional<Car> optionalCar = carService.getCar(id);

        return optionalCar.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cars")
    public List<Car> getAllCars(){
        return carService.getAllCars();
    }

    @GetMapping("/cars/{hostUserId}")
    public List<Car> getCars(@PathVariable long hostUserId) {
        return carService.getCars(hostUserId);
    }

    @GetMapping("/cars/filter")
    public List<Car> getFilteredCars(@Valid CarFilter filter) {
        System.out.println(filter);
        return carService.getFilteredCars(filter);
    }


}
