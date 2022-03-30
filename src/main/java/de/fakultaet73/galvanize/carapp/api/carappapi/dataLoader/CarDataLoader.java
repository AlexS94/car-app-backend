package de.fakultaet73.galvanize.carapp.api.carappapi.dataLoader;

import de.fakultaet73.galvanize.carapp.api.carappapi.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.CarDetails;
import de.fakultaet73.galvanize.carapp.api.carappapi.Rating;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Car;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class CarDataLoader {

    public List<Car> getCars() {
        List<Car> testCarsList = new ArrayList<>();
        Random rand = new Random();

        long[] hostUserIds = {1L, 1L, 1L, 2L, 3L, 3L, 4L, 4L, 5L, 5L};

        String[] makes = {"Volkswagen", "Mazda", "Ford", "Dodge", "Opel", "Renault", "Citroen", "Fiat"};
        String[] models = {"Golf", "Mustang", "Pinto", "X", "S Type", "Astra", "Espace", "Ram"};
        String[] types = {"Hatchback", "Convertible", "SUV", "Truck", "Sedan"};
        String[] features = {"smoke free", "pets free", "phone charger", "aux support", "nav included"};

        List<CarDetails> carDetailsList = List.of(
                new CarDetails("diesel", 5, 5, 115, "manual"),
                new CarDetails("regular", 2, 3, 310, "automatic"),
                new CarDetails("regular", 4, 5, 150, "automatic"),
                new CarDetails("electric", 4, 5, 86, "manual")
        );

        String[] guidelines = {
                "Parked behind the red building",
                "Please return after use",
                "Smoking is strongly prohibited",
                "You use it as you want",
                "This car gets you from a to b and that's about it"
        };

        String[] descriptions = {
                "Beautiful red car",
                "A little bit rusty",
                "Perfect condition",
                "Brand new car with ridiculous power"
        };

        List<Address> addressesList = List.of(
                new Address("Beispielstraße", "12", "Berlin", 12345),
                new Address("Musterstraße", "4", "Köln", 54321),
                new Address("Lindenstrasse", "1", "Wolfsburg", 11111),
                new Address("Neue Straße", "7", "Wolfsburg", 55555),
                new Address("Münchener Weg", "64", "München", 45123),
                new Address("Innenstadt", "21", "Braunschweig", 45123),
                new Address("Zum Berg", "44", "Kassel", 45123),
                new Address("Kirchenallee", "12", "Dresden", 45123),
                new Address("Gehweg", "14", "Leipzig", 45123),
                new Address("am Tor", "37", "Hannover", 45123)
        );
        for (int i = 0; i < hostUserIds.length; i++) {
            Car carTmp = Car.builder()
                    .id(i + 1)
                    .hostUserId(hostUserIds[i])
                    .make(makes[rand.nextInt(makes.length)])
                    .model(models[rand.nextInt(models.length)])
                    .type(types[rand.nextInt(types.length)])
                    .year(1960 + (int) (Math.random() * ((2022 - 1960) + 1)))
                    .details(carDetailsList.get(rand.nextInt(carDetailsList.size())))
                    .features(getRandomSubArrayFromArray(features))
                    .description(descriptions[rand.nextInt(descriptions.length)])
                    .guidelines(getRandomSubArrayFromArray(guidelines))
                    .ratings(getRandomRatings())
                    .pricePerDay(10 + (int) (Math.random() * ((50 - 10) + 1)))
                    .distancePerDay(50 + (int) (Math.random() * ((1000 - 50) + 1)))
                    .address(addressesList.get(i))
                    .build();
            testCarsList.add(carTmp);
        }
        return testCarsList;
    }

    private String[] getRandomSubArrayFromArray(String[] arr) {
        Random rand = new Random();
        int arraySize = rand.nextInt(arr.length + 1);

        String[] resultArray = new String[arraySize];
        List<Integer> previousIndexes = new ArrayList<>();
        for (int i = 0; i < arraySize; i++) {
            int randomIndex = rand.nextInt(arr.length);

            if (previousIndexes.contains(randomIndex)) {
                i--;
                continue;
            }
            resultArray[i] = arr[randomIndex];
            previousIndexes.add(randomIndex);
        }
        return resultArray;
    }

    private List<Rating> getRandomRatings() {
        List<Rating> ratingList = List.of(
                new Rating("Alice112", 4.5, LocalDate.of(2021, 1, 2), "Car was awesome!!!"),
                new Rating("Dudebro", 2.5, LocalDate.of(2021, 3, 4), "Car was meh"),
                new Rating("TravelLover22", 3.0, LocalDate.of(2020, 5, 6), "Everything was fine"),
                new Rating("NeverAgain", 4.0, LocalDate.of(2020, 7, 8), "It was ok"),
                new Rating("SomeGuy", 1.5, LocalDate.of(2022, 3, 1), "Honestly, I've seen better. It said no smoking but you could definitely smell it. That sucked."),
                new Rating("Misterx", 3.5, LocalDate.of(2022, 3, 23), "")
        );
        Random rand = new Random();
        int listSize = rand.nextInt(ratingList.size() + 1);

        List<Rating> resultList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            int randomIndex = rand.nextInt(ratingList.size());
            Rating rating = ratingList.get(randomIndex);
            if (resultList.contains(rating)) {
                i--;
                continue;
            }
            resultList.add(rating);
        }
        return resultList;
    }

}
