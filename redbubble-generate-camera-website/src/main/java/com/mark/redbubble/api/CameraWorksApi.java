package com.mark.redbubble.api;

import com.sun.javafx.geom.AreaOp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWorksApi {

    private final OkHttpClient httpClient;
    private final Request.Builder baseRequestBuilder;

    public CameraWorksApi(String cameraWorksApiUrl) {
        if (StringUtils.isBlank(cameraWorksApiUrl)) { throw new IllegalArgumentException("Provided cameraWorksApiUrl is blank"); }
        httpClient = new OkHttpClient();
        baseRequestBuilder = new Request.Builder().url(cameraWorksApiUrl);
    }

    public void getCameras() throws ApiException {
        try {
            Response cameraResponse = httpClient.newCall(baseRequestBuilder.build()).execute();
            cameraResponse.toString();
        } catch (IOException exception) {
            throw new ApiException("Exception occured while trying to get all the cameras", exception);
        }

    }

}
