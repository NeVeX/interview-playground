package com.mark.redbubble.api.model;

import org.junit.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWorksApiResponseTest {

    @Test
    public void assertCanDeserializeSingleCameraResponseAsXml() throws Exception {
        InputStream singleCameraXml = this.getClass().getResourceAsStream("/single_example_data.xml");
        assertThat(singleCameraXml).isNotNull();
        CameraWorksApiResponse cameraWorksApiResponse = CameraWorksApiResponse.convertFromStream(singleCameraXml);
        assertThat(cameraWorksApiResponse).isNotNull();
        assertThat(cameraWorksApiResponse.getWorks()).isNotNull();
        assertThat(cameraWorksApiResponse.getWorks().size()).isEqualTo(1);
        Work work = cameraWorksApiResponse.getWorks().get(0);
        assertThat(work.getId()).isEqualTo(12345);

        assertThat(work.getUrls().size()).isEqualTo(1);
        assertThat(work.getUrls().get(0).getUrl()).isEqualTo("my_url_is_here");
        assertThat(work.getUrls().get(0).getType()).isEqualTo("small");

        assertThat(work.getExif().getMake()).isEqualTo("my_make");
        assertThat(work.getExif().getModel()).isEqualTo("my_model");
    }

    @Test
    public void assertCanDeserializeMissingDataInCameraResponseAsXml() throws Exception {
        InputStream singleCameraXml = this.getClass().getResourceAsStream("/missing_example_data.xml");
        assertThat(singleCameraXml).isNotNull();
        CameraWorksApiResponse cameraWorksApiResponse = CameraWorksApiResponse.convertFromStream(singleCameraXml);
        assertThat(cameraWorksApiResponse).isNotNull(); // should still get data
        assertThat(cameraWorksApiResponse.getWorks()).isNotNull();
        assertThat(cameraWorksApiResponse.getWorks().size()).isEqualTo(1); // The entry should still be there
    }

    @Test
    public void assertCanDeserializeGivenExampleAsXml() throws Exception {
        InputStream testXml = this.getClass().getResourceAsStream("/provided_example_data.xml");
        assertThat(testXml).isNotNull();
        CameraWorksApiResponse cameraWorksApiResponse = CameraWorksApiResponse.convertFromStream(testXml);
        assertThat(cameraWorksApiResponse).isNotNull();
        assertThat(cameraWorksApiResponse.getWorks()).isNotNull();
        assertThat(cameraWorksApiResponse.getWorks().size()).isEqualTo(14);

    }


}
