package com.mark.redbubble.api;

import com.mark.redbubble.api.model.CameraWorksApiResponse;
import com.mark.redbubble.api.model.Work;
import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.model.PictureUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWorksApiClient {

    private final OkHttpClient httpClient;
    private final Request.Builder baseRequestBuilder;

    public CameraWorksApiClient(String cameraWorksApiUrl) {
        if (StringUtils.isBlank(cameraWorksApiUrl)) { throw new IllegalArgumentException("Provided cameraWorksApiUrl is blank"); }
        // Create a http client that can call out to API's
        httpClient = new OkHttpClient.Builder()
                                    .connectTimeout(5, TimeUnit.SECONDS)
                                    .readTimeout(10, TimeUnit.SECONDS)
                                    .connectTimeout(5, TimeUnit.SECONDS)
                                    .build();
        // Create the base request builder using the server endpoint given
        baseRequestBuilder = new Request.Builder().url(cameraWorksApiUrl);
    }

    public Set<CameraInformation> getCameras() throws ApiException {
        Response cameraResponse = null;
        try {
            cameraResponse = invokeCamerasFromTheApi();
            CameraWorksApiResponse cameraApiResponse = convertResponseToApiModel(cameraResponse);
            return convertApiModelToServiceModel(cameraApiResponse);
        } finally {
            if ( cameraResponse != null) { cameraResponse.close(); }
        }
    }

    private Set<CameraInformation> convertApiModelToServiceModel(CameraWorksApiResponse cameraApiResponse) {
        return cameraApiResponse.getWorks()
                .stream()
                .map(this::convertApiModelToServiceModel)
                .filter(camera -> camera != null) // Remove any that could not be converted from the API response
                .collect(Collectors.toSet());
    }

    private CameraInformation convertApiModelToServiceModel(Work work) {
        try {
            Set<PictureUrl> pictureUrls = work.getUrls()
                    .stream()
                    .map(url -> new PictureUrl(url.getUrl(), PictureUrl.PictureUrlSize.toEnum(url.getType())))
                    .collect(toSet());
            return CameraInformation.builder(work.getId())
                                    .setCameraMake(work.getExif().getMake())
                                    .setCameraModel(work.getExif().getModel())
                                    .addPictureUrls(pictureUrls)
                                    .build();
        } catch (Exception e ) {
            // Todo: log
            return null;
        }
    }

    private CameraWorksApiResponse convertResponseToApiModel(Response cameraResponse) throws ApiException {
        if ( cameraResponse.isSuccessful()) {
            try {
                return CameraWorksApiResponse.convertFromStream(cameraResponse.body().byteStream());
            } catch (ConversionException conversionException) {
                throw new ApiException(conversionException);
            }
        }
        throw new ApiException("Receieved invalid resonse from API");
    }

    private Response invokeCamerasFromTheApi() throws ApiException {
        try {
            return httpClient.newCall(baseRequestBuilder.build()).execute();
        } catch (Exception exception) {
            throw new ApiException("Exception occured while trying to get all the cameras", exception);
        }
    }

}
