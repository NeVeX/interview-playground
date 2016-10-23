package com.mark.redbubble.api;

import com.mark.redbubble.api.model.CameraWorksApiResponse;
import com.sun.javafx.geom.AreaOp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWorksApi {

    private final OkHttpClient httpClient;
    private final Request.Builder baseRequestBuilder;

    public CameraWorksApi(String cameraWorksApiUrl) {
        if (StringUtils.isBlank(cameraWorksApiUrl)) { throw new IllegalArgumentException("Provided cameraWorksApiUrl is blank"); }
        httpClient = new OkHttpClient.Builder()
                                    .connectTimeout(5, TimeUnit.SECONDS)
                                    .readTimeout(10, TimeUnit.SECONDS)
                                    .connectTimeout(5, TimeUnit.SECONDS)
                                    .build();
        baseRequestBuilder = new Request.Builder().url(cameraWorksApiUrl);
    }

    public void getCameras() throws ApiException {
        Response cameraResponse = getCamerasFromTheApi();
        convertCameraResponse(cameraResponse);

    }

    private void convertCameraResponse(Response cameraResponse) throws ApiException {
        if ( cameraResponse.isSuccessful()) {
            CameraWorksApiResponse cameras;
            try {
                cameras = new Persister().read(CameraWorksApiResponse.class, cameraResponse.body().byteStream());
            } catch (Exception exception) {
                throw new ApiException("Could not parse the API Camera response", exception);
            }
            cameras.toString(); 
        }
    }

    private Response getCamerasFromTheApi() throws ApiException {
        try {
            return httpClient.newCall(baseRequestBuilder.build()).execute();
        } catch (Exception exception) {
            throw new ApiException("Exception occured while trying to get all the cameras", exception);
        }
    }

}
