package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.model.PictureUrl;
import com.mark.redbubble.output.FileWriter;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


/**
 * Created by Mark Cunningham on 10/23/2016.
 */
public class IndexGeneratorTest {

    @Test
    public void assertIndexPageIsCreated() throws Exception {

        Path tempDirectory = Files.createTempDirectory(UUID.randomUUID().toString());
        File tempDirectoryAsFile = tempDirectory.toFile();

        FileWriter fileWriter = new FileWriter(tempDirectory);

        Set<CameraInformation> allCameras = createTestCameras();

        TemplateEngine templateEngine = new CameraWebsiteGenerator(allCameras).createTemplateEngine();

        IndexPageGenerator indexPageGenerator = new IndexPageGenerator(allCameras, templateEngine);
        // Generate the index page
        indexPageGenerator.generate(fileWriter);

        File indexFile = new File(tempDirectoryAsFile, "index.html");
        assertThat(indexFile.exists()).isTrue();

    }

    @Test
    public void assertIndexPageContainsExpectedData() throws Exception {

        Set<CameraInformation> allCameras = createTestCameras();
        TemplateEngine templateEngine = new CameraWebsiteGenerator(allCameras).createTemplateEngine();
        IndexPageGenerator indexPageGenerator = new IndexPageGenerator(allCameras, templateEngine);

        // Now just get the content that would be placed into the file
        String content = indexPageGenerator.createContent();
        assertThat(content).contains("make_one");
        assertThat(content).contains("make_two");

        // Only two pictures will show, since it will pick the smallest thumbnail for each of the 4 given
        assertThat(content).contains("picture_two.jpg");
        assertThat(content).contains("picture_three.jpg");
    }

    private Set<CameraInformation> createTestCameras() {
        Set<CameraInformation> allCameras = new HashSet<>();

        CameraInformation cameraOne = CameraInformation.builder(1234)
                .withCameraMake("make_one")
                .withCameraModel("model_one")
                .addPictureUrl(new PictureUrl("picture_one.jpg", PictureUrl.PictureUrlSize.Large))
                .addPictureUrl(new PictureUrl("picture_two.jpg", PictureUrl.PictureUrlSize.Medium))
                .build();

        CameraInformation cameraTwo = CameraInformation.builder(666)
                .withCameraMake("make_two")
                .withCameraModel("model_two")
                .addPictureUrl(new PictureUrl("picture_three.jpg", PictureUrl.PictureUrlSize.Small))
                .addPictureUrl(new PictureUrl("picture_four.jpg", PictureUrl.PictureUrlSize.Medium))
                .build();

        allCameras.add(cameraOne);
        allCameras.add(cameraTwo);

        return allCameras;
    }
}
