package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.model.PictureUrl;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mark Cunningham on 10/25/2016.
 */
class GeneratorTestUtils {

    /**
     * Helper method to create test camera data
     * @return - set of camera info
     */
    static Set<CameraInformation> createTestCameras() {
        Set<CameraInformation> allCameras = new HashSet<>();

        CameraInformation cameraOne = CameraInformation.builder(1234)
                .withCameraMake("make_one")
                .withCameraModel("model_one")
                .addPictureUrl(new PictureUrl("picture_one.jpg", PictureUrl.PictureUrlSize.Large))
                .addPictureUrl(new PictureUrl("picture_two.jpg", PictureUrl.PictureUrlSize.Medium))
                .build();

        CameraInformation cameraTwo = CameraInformation.builder(666)
                .withCameraMake("make_two")
                .withCameraModel("model_two")
                .addPictureUrl(new PictureUrl("picture_three.jpg", PictureUrl.PictureUrlSize.Small))
                .addPictureUrl(new PictureUrl("picture_four.jpg", PictureUrl.PictureUrlSize.Medium))
                .build();

        allCameras.add(cameraOne);
        allCameras.add(cameraTwo);

        return allCameras;
    }

}
