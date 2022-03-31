package de.fakultaet73.galvanize.carapp.api.carappapi.dataLoader;

import de.fakultaet73.galvanize.carapp.api.carappapi.enums.ReferenceType;
import de.fakultaet73.galvanize.carapp.api.carappapi.documents.ImageFile;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageFileDataLoader {

    public List<ImageFile> getImageFiles() throws IOException {
        List<ImageFile> testImageFilesList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            File file = new File(String.format("testResources/images/image%d.jpg", i));
            ImageFile imageFileTmp = ImageFile.builder()
                    .id(i)
                    .referenceId((long) i)
                    .type(ReferenceType.USER)
                    .name(file.getName())
                    .createdTime(LocalDate.now())
                    .content(new Binary(new FileInputStream(file).readAllBytes()))
                    .contentType("image/jpeg")
                    .size(file.length())
                    .build();
            testImageFilesList.add(imageFileTmp);
        }

        for (int i = 11; i <= 20; i++) {
            File file = new File(String.format("testResources/images/image%d.jpg", i));
            ImageFile imageFileTmp = ImageFile.builder()
                    .id(i)
                    .referenceId((long) i - 10)
                    .type(ReferenceType.CAR)
                    .name(file.getName())
                    .createdTime(LocalDate.now())
                    .content(new Binary(new FileInputStream(file).readAllBytes()))
                    .contentType("image/jpeg")
                    .size(file.length())
                    .build();
            testImageFilesList.add(imageFileTmp);
        }

        return testImageFilesList;
    }

}
