package com.mark.redbubble.api;

import org.junit.Test;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWorksApiClientTest {

    private static final String EXAMPLE_API_URL = "http://take-home-test.herokuapp.com/api/v1/works.xml";

    @Test
    public void assertTheGivenExampleApiWorks() throws Exception {
        CameraWorksApiClient cameraWorksApiClient = new CameraWorksApiClient(EXAMPLE_API_URL);
        cameraWorksApiClient.getCameras();
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
