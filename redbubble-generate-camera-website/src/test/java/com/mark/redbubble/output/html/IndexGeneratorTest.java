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
public class IndexGeneratorTest {

    @Test
    public void assertIndexPageIsCreated() throws Exception {

        Path tempDirectory = Files.createTempDirectory(UUID.randomUUID().toString());
        File tempDirectoryAsFile = tempDirectory.toFile();

        OutputWriter outputWriter = new OutputWriter(tempDirectory);

        Set<CameraInformation> allCameras = GeneratorTestUtils.createTestCameras();

        TemplateEngine templateEngine = new CameraWebsiteGenerator(allCameras).createTemplateEngine();

        IndexPageGenerator indexPageGenerator = new IndexPageGenerator(allCameras, templateEngine);
        // Generate the index page
        indexPageGenerator.generate(outputWriter);

        File indexFile = new File(tempDirectoryAsFile, "index.html");
        assertThat(indexFile.exists()).isTrue();

    }

    @Test
    public void assertIndexPageContainsExpectedData() throws Exception {

        Set<CameraInformation> allCameras = GeneratorTestUtils.createTestCameras();
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

}
