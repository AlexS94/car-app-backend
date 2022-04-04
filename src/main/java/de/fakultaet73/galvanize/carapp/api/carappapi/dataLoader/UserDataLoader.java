package de.fakultaet73.galvanize.carapp.api.carappapi.dataLoader;

import de.fakultaet73.galvanize.carapp.api.carappapi.model.Address;
import de.fakultaet73.galvanize.carapp.api.carappapi.model.Rating;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.SequenceGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Component
public class UserDataLoader {

    SequenceGeneratorService sequenceGeneratorService;


    public List<User> getUsers() {
        List<User> testUsersList = new ArrayList<>();

        String[] firstNameArray = {"David", "Alex", "Marcel", "Marian", "Manfred", "Bob", "Anna", "Susan", "Jasmine", "Nadine"};
        String[] lastNamesArray = {"Muller", "Schulze", "Thompson", "ONeil", "Meyer", "Bobson", "York", "Myers", "Fisher", "Smith"};
        String[] userNameArray = {"firefox", "MrAlex", "Marci777", "someDude", "SmartDude", "bob123", "Anni", "Susi87", "Foxi221", "NaddiX"};
        String[] emailsArray = {"muller@web.de", "schulze@icloud.com", "thompson@mail.com", "oneil@yahoo.de", "meyer@aol.com", "bobson@bob.com", "anni@gmail.com", "SusiM@mail.com", "Fisher@yahoo.com", "SmithN@gmail.com"};
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
        for (int i = 0; i < firstNameArray.length; i++) {
            User userTmp = User.builder()
                    .firstName(firstNameArray[i])
                    .lastName(lastNamesArray[i])
                    .userName(userNameArray[i])
                    .email(emailsArray[i])
                    .password(lastNamesArray[i] + i)
                    .dateOfBirth(LocalDate.of(1999 - i, i + 1, i + 1))
                    .address(addressesList.get(i))
                    .ratings(getRandomRatings())
                    .build();
            userTmp.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
            testUsersList.add(userTmp);
        }
        return testUsersList;
    }

    private List<Rating> getRandomRatings() {
        List<Rating> ratingList = List.of(
                new Rating("Alice112", 4.5, LocalDate.of(2022, 2, 2), "Host was awesome!!!"),
                new Rating("Dudebro", 2.5, LocalDate.of(2022, 3, 23), "Communication was meh"),
                new Rating("TravelLover22", 3.0, LocalDate.of(2022, 1, 2), "Everything was fine"),
                new Rating("NeverAgain", 4.0, LocalDate.of(2021, 11, 14), "Everything went as expected"),
                new Rating("SomeGuy", 1.5, LocalDate.of(2021, 11, 3), "Honestly, I've had better experiences. Host only responded like once a day"),
                new Rating("Misterx", 3.5, LocalDate.of(2021, 10, 18), "")
        );
        Random rand = new Random();
        int listSize = rand.nextInt(ratingList.size() + 1);

        List<Rating> resultList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            int randomIndex = rand.nextInt(ratingList.size());
            resultList.add(ratingList.get(randomIndex));
        }
        return resultList;
    }

}
