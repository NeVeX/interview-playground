package com.mark.redbubble.api;

import org.junit.Test;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWorksApiTest {

    private static final String EXAMPLE_API_URL = "http://take-home-test.herokuapp.com/api/v1/works.xml";

    @Test
    public void assertTheGivenExampleApiWorks() throws Exception {
        CameraWorksApi cameraWorksApi = new CameraWorksApi(EXAMPLE_API_URL);
        cameraWorksApi.getCameras();
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownOnEmptyApiUrl() throws Exception {
        new CameraWorksApi("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertExceptionThrownOnNullApiUrl() throws Exception {
        new CameraWorksApi(null);
    }

}
