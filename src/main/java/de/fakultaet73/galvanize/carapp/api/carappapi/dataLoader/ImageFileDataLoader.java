package de.fakultaet73.galvanize.carapp.api.carappapi.dataLoader;

import de.fakultaet73.galvanize.carapp.api.carappapi.documents.Booking;
import de.fakultaet73.galvanize.carapp.api.carappapi.enums.ReferenceType;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.ImageFile;
import de.fakultaet73.galvanize.carapp.api.carappapi.services.SequenceGeneratorService;
import lombok.AllArgsConstructor;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class ImageFileDataLoader {

    SequenceGeneratorService sequenceGeneratorService;

    public List<ImageFile> getImageFiles() throws IOException {
        List<ImageFile> testImageFilesList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            File file = new File(String.format("testResources/images/image%d.jpg", i));
            ImageFile imageFileTmp = ImageFile.builder()
                    .referenceId((long) i)
                    .type(ReferenceType.USER)
                    .name(file.getName())
                    .createdTime(LocalDate.now())
                    .content(new Binary(new FileInputStream(file).readAllBytes()))
                    .contentType("image/jpeg")
                    .size(file.length())
                    .build();
            imageFileTmp.setId(sequenceGeneratorService.generateSequence(ImageFile.SEQUENCE_NAME));
            testImageFilesList.add(imageFileTmp);
        }

        for (int i = 11; i <= 20; i++) {
            File file = new File(String.format("testResources/images/image%d.jpg", i));
            ImageFile imageFileTmp = ImageFile.builder()
                    .referenceId((long) i - 10)
                    .type(ReferenceType.CAR)
                    .name(file.getName())
                    .createdTime(LocalDate.now())
                    .content(new Binary(new FileInputStream(file).readAllBytes()))
                    .contentType("image/jpeg")
                    .size(file.length())
                    .build();
            imageFileTmp.setId(sequenceGeneratorService.generateSequence(ImageFile.SEQUENCE_NAME));
            testImageFilesList.add(imageFileTmp);
        }

        return testImageFilesList;
    }

}
