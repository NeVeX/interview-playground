package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.output.HtmlFileWriter;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWebsiteGeneratorTest {

    @Test
    public void assertIndexHtmlCreation() {
        CameraInformation cameraInformation = CameraInformation.builder(1234)
                .withCameraMake("my_make").withCameraModel("my_model").build();
        Set<CameraInformation> cameras = new HashSet<>();
        cameras.add(cameraInformation);

        HtmlFileWriter htmlFileWriter = new HtmlFileWriter(new File("c:\\Temp\\"));

        new CameraWebsiteGenerator(cameras, htmlFileWriter).testing();



    }

}
