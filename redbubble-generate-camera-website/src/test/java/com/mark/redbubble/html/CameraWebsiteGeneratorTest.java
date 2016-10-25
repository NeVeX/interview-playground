package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.model.PictureUrl;
import com.mark.redbubble.output.OutputWriter;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWebsiteGeneratorTest {

    @Test
    public void assertIndexHtmlCreation() throws Exception {
        CameraInformation cameraInformation = CameraInformation.builder(1234)
                .withCameraMake("my_make")
                .withCameraModel("my_model")
                .addPictureUrl(new PictureUrl("http://ih1.redbubble.net/work.31820.1.flat,550x550,075,f.jpg", PictureUrl.PictureUrlSize.Medium))
                .addPictureUrl(new PictureUrl("http://ih1.redbubble.net/work.31820.1.flat,550x550,075,f.jpg", PictureUrl.PictureUrlSize.Medium))
                .addPictureUrl(new PictureUrl("http://ih1.redbubble.net/work.31820.1.flat,550x550,075,f.jpg", PictureUrl.PictureUrlSize.Medium))
                .addPictureUrl(new PictureUrl("http://ih1.redbubble.net/work.31820.1.flat,550x550,075,f.jpg", PictureUrl.PictureUrlSize.Medium))
                .addPictureUrl(new PictureUrl("http://ih1.redbubble.net/work.31820.1.flat,550x550,075,f.jpg", PictureUrl.PictureUrlSize.Medium))
                .addPictureUrl(new PictureUrl("http://ih1.redbubble.net/work.31820.1.flat,550x550,075,f.jpg", PictureUrl.PictureUrlSize.Medium))
                .build();

        Set<CameraInformation> cameras = new HashSet<>();
        cameras.add(cameraInformation);

        Path tempDirectory = Files.createTempDirectory(UUID.randomUUID().toString());
        File tempDirectoryAsFile = tempDirectory.toFile();
        assertThat(tempDirectoryAsFile.listFiles().length).isEqualTo(0); // No files
        OutputWriter outputWriter = new OutputWriter(tempDirectory);

        CameraWebsiteGenerator cameraWebsiteGenerator = new CameraWebsiteGenerator(cameras);

        cameraWebsiteGenerator.generate(outputWriter);
        // We expect 3 files and the 2 folders
        assertThat(tempDirectoryAsFile.listFiles().length).isEqualTo(5);

        assertThat(tempDirectoryAsFile.list()).contains("index.html");
        assertThat(tempDirectoryAsFile.list()).contains("my_make.html");
        assertThat(tempDirectoryAsFile.list()).contains("my_model.html");
        assertThat(tempDirectoryAsFile.list()).contains("scripts");
        assertThat(tempDirectoryAsFile.list()).contains("styles");

        // Make sure the styles has the correct files
        File stylesDirectory = new File(tempDirectoryAsFile, "styles");
        assertThat(stylesDirectory.listFiles().length).isEqualTo(1);
        assertThat(stylesDirectory.list()).contains("main.css");

        File scriptDirectory = new File(tempDirectoryAsFile, "scripts");
        assertThat(scriptDirectory.listFiles().length).isEqualTo(2);
        assertThat(scriptDirectory.list()).contains("jquery-3.1.1.min.js");
        assertThat(scriptDirectory.list()).contains("selection_control.js");

        // Remove everything - again - clean house

    }

}
