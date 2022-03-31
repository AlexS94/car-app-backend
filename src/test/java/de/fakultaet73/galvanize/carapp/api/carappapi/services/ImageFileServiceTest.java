package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.enums.ReferenceType;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.ImageFile;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.CarNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserImageAlreadyExists;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.ImageFileRepository;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageFileServiceTest {

    ImageFileService imageFileService;

    @Mock
    ImageFileRepository imageFileRepository;

    @Mock
    UserService userService;

    @Mock
    CarService carService;

    @Mock
    SequenceGeneratorService sequenceGeneratorService;

    ImageFile userImageFile;
    ImageFile carImageFile;

    @BeforeEach
    void setUp() throws Exception {
        imageFileService = new ImageFileService(imageFileRepository, userService, carService, sequenceGeneratorService);
        File file = new File("testResources/images/image1.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), MediaType.IMAGE_JPEG_VALUE, fileInputStream);

        userImageFile = ImageFile.builder()
                .referenceId(1L)
                .type(ReferenceType.USER)
                .name("image1")
                .createdTime(LocalDate.now())
                .content(new Binary(multipartFile.getBytes()))
                .contentType(multipartFile.getContentType())
                .size(multipartFile.getSize()).build();

        carImageFile = ImageFile.builder()
                .referenceId(1L)
                .type(ReferenceType.CAR)
                .name("image1")
                .createdTime(LocalDate.now())
                .content(new Binary(multipartFile.getBytes()))
                .contentType(multipartFile.getContentType())
                .size(multipartFile.getSize()).build();
    }

    @Test
    void addImageFile_ImageFile_void() {
        // Arrange
        when(userService.userExists(anyLong())).thenReturn(true);
        when(imageFileRepository.existsImageFileByReferenceIdAndType(anyLong(), any(ReferenceType.class))).thenReturn(false);
        when(imageFileRepository.save(any(ImageFile.class))).thenReturn(userImageFile);

        // Act
        imageFileService.uploadImageFile(userImageFile);

        // Assert‰
        verify(imageFileRepository).save(any(ImageFile.class));
    }

    @Test
    void addImageFile_ImageFile_userNotExists_throwsUserNotExistsException() {
        // Arrange
        when(userService.userExists(anyLong())).thenReturn(false);

        // Act // Assert
        assertThrows(UserNotExistsException.class,
                () -> imageFileService.uploadImageFile(userImageFile),
                "Exception was expected");
        verify(imageFileRepository, never()).save(any(ImageFile.class));
    }

    @Test
    void addImageFile_ImageFile_userImageAlreadyExists_throwsUserImageAlreadyExistsException() {
        // Arrange
        when(userService.userExists(anyLong())).thenReturn(true);
        when(imageFileRepository.existsImageFileByReferenceIdAndType(anyLong(), any(ReferenceType.class))).thenReturn(true);

        // Act // Assert
        assertThrows(UserImageAlreadyExists.class,
                () -> imageFileService.uploadImageFile(userImageFile),
                "Exception was expected");
    }

    @Test
    void addImageFile_ImageFile_carNotExists_throwsCarNotExistsException(){
        // Arrange
        when(carService.carExists(anyLong())).thenReturn(false);

        // Act // Assert
        assertThrows(CarNotExistsException.class,
                () -> imageFileService.uploadImageFile(carImageFile),
                "Exception was expected");
    }

    @Test
    void deleteImageFile_id_returnsTrue() {
        // Arrange
        when(imageFileRepository.existsImageFileById(anyLong())).thenReturn(true);
        doNothing().when(imageFileRepository).deleteById(anyLong());

        // Act
        boolean result = imageFileService.deleteImageFile(userImageFile.getId());

        // Assert
        assertTrue(result);
        verify(imageFileRepository).deleteById(anyLong());
        verify(imageFileRepository).existsImageFileById(anyLong());
    }

    @Test
    void deleteImageFile_id_notExists_returnsFalse() {
        // Arrange
        when(imageFileRepository.existsImageFileById(anyLong())).thenReturn(false);

        // Act
        boolean result = imageFileService.deleteImageFile(userImageFile.getId());

        // Assert
        assertFalse(result);
        verify(imageFileRepository).existsImageFileById(anyLong());
    }

}