package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.ImageFile;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.CarNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserImageAlreadyExists;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.BookingService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.CarService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.ImageFileService;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class ImageFileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CarService carService;

    @MockBean
    UserService userService;

    @MockBean
    BookingService bookingService;

    @MockBean
    ImageFileService imageFileService;

    @MockBean
    ModelMapper modelMapper;

    MockMultipartFile multipartFile;

    @BeforeEach
    void setUp() throws Exception {
        File file = new File("testRessources/image1.jpeg");
        FileInputStream fileInputStream = new FileInputStream(file);
        multipartFile = new MockMultipartFile("file", file.getName(), MediaType.IMAGE_JPEG_VALUE, fileInputStream);
    }

    @Test
    void addImageFile_MultipartFile_returnsOk() throws Exception {
        // Act
        mockMvc.perform(multipart("/file/image")
                        .file(multipartFile)
                        .param("referenceId", "1")
                        .param("type", "USER"))
                // Assert
                .andExpect(status().isOk());
        verify(imageFileService).uploadImageFile(any(ImageFile.class));
    }

    @Test
    void addImageFile_invalidType_returnsBadRequest() throws Exception {
        // Act
        mockMvc.perform(multipart("/file/image")
                        .file(multipartFile)
                        .param("type", "INVALIDTYPE")
                        .param("referenceId", "1"))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void addImageFile_userNotExists_returnsBadRequest() throws Exception {
        // Arrange
        doThrow(new UserNotExistsException("")).when(imageFileService).uploadImageFile(any(ImageFile.class));

        // Act
        mockMvc.perform(multipart("/file/image")
                        .file(multipartFile)
                        .param("type", "USER")
                        .param("referenceId", "1"))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void addImageFile_userImageAlreadyExists_returnsConflict() throws Exception {
        // Arrange
        doThrow(new UserImageAlreadyExists("")).when(imageFileService).uploadImageFile(any(ImageFile.class));

        // Act
        mockMvc.perform(multipart("/file/image")
                        .file(multipartFile)
                        .param("type", "USER")
                        .param("referenceId", "1"))
                // Assert
                .andExpect(status().isConflict());
    }

    @Test
    void addImageFile_carNotExists_returnsBadRequest() throws Exception {
        // Arrange
        doThrow(new CarNotExistsException("")).when(imageFileService).uploadImageFile(any(ImageFile.class));

        // Act
        mockMvc.perform(multipart("/file/image")
                        .file(multipartFile)
                        .param("type", "CAR")
                        .param("referenceId", "1"))
                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteImageFile_id_returnsOk() throws Exception {
        // Assert
        when(imageFileService.deleteImageFile(anyLong())).thenReturn(true);

        // Act
        mockMvc.perform(delete("/file/image/1"))
                // Assert
                .andExpect(status().isOk());
    }

    @Test
    void deleteImageFile_id_notFound_returnsNoContent() throws Exception {
        // Assert
        when(imageFileService.deleteImageFile(anyLong())).thenReturn(false);

        // Act
        mockMvc.perform(delete("/file/image/1"))
                // Assert
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteImageFile_noParam_returnsMethodNotAllowed() throws Exception {
        // Act
        mockMvc.perform(delete("/file/image"))
                // Assert
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void deleteImageFile_wrongFormat_returnsBadRequest() throws Exception {
        // Act
        mockMvc.perform(delete("/car/aa"))
                // Assert
                .andExpect(status().isBadRequest());
    }

}
