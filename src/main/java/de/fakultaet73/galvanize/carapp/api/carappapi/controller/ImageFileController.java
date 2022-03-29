package de.fakultaet73.galvanize.carapp.api.carappapi.controller;

import de.fakultaet73.galvanize.carapp.api.carappapi.ReferenceType;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.ImageFile;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.CarNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserImageAlreadyExists;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.ImageFileService;
import lombok.AllArgsConstructor;
import org.bson.types.Binary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@AllArgsConstructor
@RestController
public class ImageFileController {

    ImageFileService imageFileService;

    @PostMapping(value = "/file/image")
    public ResponseEntity<String> uploadImageFile(
            @RequestParam long referenceId,
            @RequestParam ReferenceType type,
            @RequestParam MultipartFile file) {

        try {
            ImageFile imageFile = ImageFile.builder()
                    .referenceId(referenceId)
                    .type(type)
                    .name(file.getName())
                    .createdTime(LocalDate.now())
                    .content(new Binary(file.getBytes()))
                    .contentType(file.getContentType())
                    .size(file.getSize()).build();
            imageFileService.uploadImageFile(imageFile);
        } catch (IOException iox) {
            return ResponseEntity.internalServerError().body("image upload failed");
        }
        return ResponseEntity.ok("image upload success");
    }

    // TODO put mapping

    @DeleteMapping("/file/image/{id}")
    public ResponseEntity<String> deleteImageFile(@PathVariable long id) {
        return imageFileService.deleteImageFile(id) ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void UserNotExistsExceptionHandler(UserNotExistsException exception) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void CarNotExistsExceptionHandler(CarNotExistsException exception) {
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public void UserImageAlreadyExistsExceptionHandler(UserImageAlreadyExists exception) {
    }

}