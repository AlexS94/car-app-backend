package de.fakultaet73.galvanize.carapp.api.carappapi.services;

import de.fakultaet73.galvanize.carapp.api.carappapi.ReferenceType;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.ImageFile;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.CarNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserImageAlreadyExists;
import de.fakultaet73.galvanize.carapp.api.carappapi.exceptions.UserNotExistsException;
import de.fakultaet73.galvanize.carapp.api.carappapi.repositories.ImageFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ImageFileService {

    ImageFileRepository imageFileRepository;
    UserService userService;
    CarService carService;
    SequenceGeneratorService sequenceGeneratorService;

    public Optional<ImageFile> getImageFile(long id, ReferenceType type) {
        return imageFileRepository.findFirstByReferenceIdAndType(id, type);
    }

    public List<ImageFile> getImageFiles(long referenceId, ReferenceType type) {
        return imageFileRepository.findAllByReferenceIdAndType(referenceId, type);
    }

    public void uploadImageFile(ImageFile imageFile) {
        validateImageFile(imageFile);
        imageFile.setId(sequenceGeneratorService.generateSequence(ImageFile.SEQUENCE_NAME));
        imageFileRepository.save(imageFile);
    }

    public boolean deleteImageFile(long id) {
        if (imageFileExists(id)) {
            imageFileRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAllWithReferenceIdAndType(long referenceId, ReferenceType type) {
        imageFileRepository.deleteAllByReferenceIdAndType(referenceId, type);
    }

    private void validateImageFile(ImageFile imageFile) {
        switch (imageFile.getType()) {
            case USER:
                if (!userService.userExists(imageFile.getReferenceId())) {
                    throw new UserNotExistsException("User does not exist");
                }
                if (userImageExists(imageFile.getReferenceId())) {
                    throw new UserImageAlreadyExists("User already has profile picture");
                }
                break;
            case CAR:
                if (!carService.carExists(imageFile.getReferenceId())) {
                    throw new CarNotExistsException("Car does not exist");
                }
                break;
            default:
        }
    }

    public boolean userImageExists(long userId) {
        return imageFileRepository.existsImageFileByReferenceIdAndType(userId, ReferenceType.USER);
    }

    public boolean imageFileExists(long id) {
        return imageFileRepository.existsImageFileById(id);
    }

}
