package com.mark.redbubble;

import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Files;
import java.util.UUID;

/**
 * Created by Mark Cunningham on 10/23/2016.
 */
@Ignore // This test is more an integration test, so not using it here (it's good for manual smoke testing)
public class CameraWebsiteCreatorApplicationTest {

    @Test
    public void doSmokeTestOfApplication() throws Exception {
        String[] args = new String[]{
            "-a=http://take-home-test.herokuapp.com/api/v1/works.xml",
            "-o="+ Files.createTempDirectory(UUID.randomUUID().toString()).toAbsolutePath() // random location
        };
        new CameraWebsiteCreatorApplication().run(args);
    }

}
