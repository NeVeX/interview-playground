package com.mark.redbubble.api;

import com.mark.redbubble.model.CameraInformation;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWorksApiClientTest {

    private static final String EXAMPLE_API_URL = "http://take-home-test.herokuapp.com/api/v1/works.xml";

    @Test
    public void assertTheGivenExampleApiWorks() throws Exception {
        CameraWorksApiClient cameraWorksApiClient = new CameraWorksApiClient(EXAMPLE_API_URL);
        Set<CameraInformation> cameras = cameraWorksApiClient.getCameras();
        assertThat(cameras).isNotNull();
        assertThat(cameras).isNotEmpty(); // Don't explicitly check the size, since the example API may change
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownOnEmptyApiUrl() throws Exception {
        new CameraWorksApiClient("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownOnNullApiUrl() throws Exception {
        new CameraWorksApiClient(null);
    }

}
