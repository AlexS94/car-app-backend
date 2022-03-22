package de.fakultaet73.galvanize.carapp.api.carappapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.User;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarAppApiApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    List<User> testUserList;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        testUserList = new ArrayList<>();
        fillTestUsersList();
        userRepository.saveAll(testUserList);
    }

    private void fillTestUsersList() {


        String[] firstNameArray = {"David", "Alex", "Marcel", "Marian", "Manfred"};
        String[] userNameArray = {"firefox", "Ddge", "JohnStar", "LowBoy", "SmartDude"};
        String[] emailsArray = {"muller@web.de", "schulze@icloud.com", "thompson@mail.com", "oneil@yahoo.de", "meyer@aol.com"};
        String[] lastNamesArray = {"Muller", "Schulze", "Thompson", "ONeil", "Meyer"};
        List<Address> addressesList = List.of(new Address("Examplestreet", 12, "Berlin", 12345),
                new Address("Musterstraße", 4, "Munich", 54321),
                new Address("Lindenstrasse", 1, "Wolfsburg", 11111),
                new Address("Upstate", 335, "New York", 55555),
                new Address("Compton Blvd", 83, "Los Angeles", 45123));

        for (int i = 0; i < 5; i++) {
            User userTmp = User.builder()
                    .id(i)
                    .firstName(firstNameArray[i])
                    .lastName(lastNamesArray[i])
                    .userName(userNameArray[i])
                    .email(emailsArray[i])
                    .password("password")
                    .dateOfBirth(LocalDate.of(1999 - i, i + 1, i + 1))
                    .address(addressesList.get(i))
                    .build();
            testUserList.add(userTmp);
        }
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    void checkDatabase() {
        // Arrange
        List<User> userFromDatabaseList = new ArrayList<>();

        // Act
        userFromDatabaseList = userRepository.findAll();

        //Assert
        for (int i = 0; i < userFromDatabaseList.size(); i++) {
            User user = userFromDatabaseList.get(i);
            System.out.println(user.toString());
            assertEquals(user.getUserName(), testUserList.get(i).getUserName());
        }
    }

    @Test
    void getUser_id_returnsUser() throws Exception {
        // Act
        ResponseEntity<User> response = restTemplate.getForEntity("/user/1", User.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEquals(testUserList.get(0).getUserName(), response.getBody().getUserName());
    }

    @Test
    void validate_with_Password_Email() throws Exception {
        // Arrange
        String url = "/validate?input=" + testUserList.get(0).getEmail() + "&password=" + testUserList.get(0).getPassword();

        //Act
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEquals(testUserList.get(0).getUserName(), response.getBody().getUserName());
    }

    @Test
    void validate_with_Password_UserName() throws Exception {
        // Arrange
        String url = "/validate?input=" + testUserList.get(0).getUserName() + "&password=" + testUserList.get(0).getPassword();

        //Act
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEquals(testUserList.get(0).getUserName(), response.getBody().getUserName());
    }

    @Test
    void add_User() throws JsonProcessingException {
        // Arrange
        User userTmp = User.builder()
                .firstName("Vorname")
                .lastName("Nachname")
                .userName("Nutzername")
                .email("email@22222222.de")
                .password("password")
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .address(new Address("Examplestreet", 12, "Berlin", 12345))
                .build();

        String json =   mapper.writeValueAsString(userTmp);
        System.out.println(json);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<User> request = new HttpEntity<>(userTmp, headers);

        // Act
        ResponseEntity<User> response = restTemplate.postForEntity("/user", request, User.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void contextLoads() {

    }


}
