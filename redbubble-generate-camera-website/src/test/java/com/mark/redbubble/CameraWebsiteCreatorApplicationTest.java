package com.mark.redbubble;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Mark Cunningham on 10/23/2016.
 */
public class CameraWebsiteCreatorApplicationTest {

    @Test
    public void doSmokeTestOfApplication() throws Exception {
        String[] args = new String[]{
            "-a=http://take-home-test.herokuapp.com/api/v1/works.xml",
            "-o=C:\\Temp\\integration_test\\"
        };
        new CameraWebsiteCreatorApplication().run(args);
    }


}
