package com.mark.redbubble.output.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.output.OutputWriter;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


/**
 * Created by Mark Cunningham on 10/23/2016.
 */
public class CameraMakePageGeneratorTest {

    @Test
    public void assertMakePageIsCreated() throws Exception {

        Path tempDirectory = Files.createTempDirectory(UUID.randomUUID().toString());
        File tempDirectoryAsFile = tempDirectory.toFile();

        OutputWriter outputWriter = new OutputWriter(tempDirectory);

        Set<CameraInformation> allCameras = GeneratorTestUtils.createTestCameras();

        TemplateEngine templateEngine = new CameraWebsiteGenerator(allCameras).createTemplateEngine();

        CameraMakePageGenerator cameraMakePageGenerator = new CameraMakePageGenerator(allCameras, templateEngine);
        // Generate the index page
        cameraMakePageGenerator.generate(outputWriter);

        assertThat(tempDirectoryAsFile.listFiles().length).isEqualTo(2); // 2 makes expected only
        assertThat(tempDirectoryAsFile.list()).contains("make_one.html");
        assertThat(tempDirectoryAsFile.list()).contains("make_two.html");

    }

    @Test
    public void assertIndexPageContainsExpectedData() throws Exception {

        Set<CameraInformation> allCameras = GeneratorTestUtils.createTestCameras();
        TemplateEngine templateEngine = new CameraWebsiteGenerator(allCameras).createTemplateEngine();
        CameraMakePageGenerator cameraMakePageGenerator = new CameraMakePageGenerator(allCameras, templateEngine);

        // Now just get the content that would be placed into the file
        String content = cameraMakePageGenerator.createContent("my-fancy-make", allCameras);
        assertThat(content).contains("My-fancy-make");
        assertThat(content).contains("index.html"); // the index page link
        assertThat(content).contains("model_one");
        assertThat(content).contains("model_one.html");
        assertThat(content).contains("model_two");
        assertThat(content).contains("model_two.html");

        // Only two pictures will show, since it will pick the smallest thumbnail for each of the 4 given
        assertThat(content).contains("picture_two.jpg");
        assertThat(content).contains("picture_three.jpg");
    }

}
