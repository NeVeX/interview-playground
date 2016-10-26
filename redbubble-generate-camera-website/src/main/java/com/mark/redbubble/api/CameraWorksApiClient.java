package com.mark.redbubble.api;

import com.mark.redbubble.api.model.CameraWorksApiResponse;
import com.mark.redbubble.api.model.Work;
import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.model.PictureUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * Created by Mark Cunningham on 10/22/2016.
 * <br>This class represents functions that can be leveraged to communicate with the Camera's API exif data
 */
public class CameraWorksApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CameraWorksApiClient.class);
    private final OkHttpClient httpClient;
    private final Request.Builder baseRequestBuilder;

    /**
     * Given the valid URL of the API to use, this will initialize everything needed to consume the resource
     * @param cameraWorksApiUrl - the valid API url to use
     */
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

    /**
     * Simple method that will invoke the camera API and extract all valid camera information
     * @return - the set of camera information extracted from the API
     * @throws ApiException - if anything goes wrong talking to the API
     */
    public Set<CameraInformation> getCameras() throws ApiException {
        Response cameraResponse = null;
        try {
            cameraResponse = invokeCamerasFromTheApi(); // Invoke the API
            CameraWorksApiResponse cameraApiResponse = convertResponseToApiModel(cameraResponse); // Convert the response
            return convertApiModelToServiceModel(cameraApiResponse); // Return the POJO representation
        } finally {
            if ( cameraResponse != null) { cameraResponse.close(); }
        }
    }

    /**
     * Given the converted model object that represents an API response; this method will convert it to the
     * application model {@link CameraInformation}
     * @param cameraApiResponse - the representation of an API response
     * @return the set of valid extracted camera information
     */
    private Set<CameraInformation> convertApiModelToServiceModel(CameraWorksApiResponse cameraApiResponse) {
        return cameraApiResponse.getWorks()
                .parallelStream()
                .map(this::convertApiModelToServiceModel)
                .filter(camera -> camera != null) // Remove any that could not be converted from the API response
                .collect(Collectors.toSet());
    }

    /**
     * An extension of the {{@link #convertApiModelToServiceModel(CameraWorksApiResponse)}}, this method takes
     * and individual entry of a camera to convert it
     * @param work - the API representation of a camera's work
     * @return - the application model representation of the camera
     */
    private CameraInformation convertApiModelToServiceModel(Work work) {
        try {
            // Build up the set of picture urls that are in the response
            Set<PictureUrl> pictureUrls = work.getUrls()
                    .parallelStream()
                    .map(url -> new PictureUrl(url.getUrl(), PictureUrl.PictureUrlSize.toEnum(url.getType())))
                    .collect(toSet());
            // Now build the full camera info object using the provided builder
            return CameraInformation.builder(work.getId())
                                    .withCameraMake(work.getExif().getMake())
                                    .withCameraModel(work.getExif().getModel())
                                    .addPictureUrls(pictureUrls)
                                    .build();
        } catch (Exception exception) {
            // Cannot parse this, so log a warning message and return null
            LOGGER.warn("Could not convert API data [{}] into model class [{}]", work, CameraInformation.class);
            return null;
        }
    }

    /**
     * Given the raw {@link OkHttpClient} respsone, use the model conversion provided in {@link CameraWorksApiResponse}
     * to convert the XML response into the java model
     * @param cameraResponse- the raw xml response
     * @return - the java model representing the response
     * @throws ApiException - if the conversion failed or the response is not valid
     */
    private CameraWorksApiResponse convertResponseToApiModel(Response cameraResponse) throws ApiException {
        if ( cameraResponse.isSuccessful()) {
            try {
                return CameraWorksApiResponse.convertFromStream(cameraResponse.body().byteStream());
            } catch (ConversionException conversionException) {
                throw new ApiException(conversionException);
            }
        }
        throw new ApiException("Received invalid response from API");
    }

    /**
     * Helper method to invoke the API and get a response - all exceptions are wrapped into the {@link ApiException}
     * @return - the raw response from the API
     * @throws ApiException - if there was any problems talking to the API
     */
    private Response invokeCamerasFromTheApi() throws ApiException {
        try {
            return httpClient.newCall(baseRequestBuilder.build()).execute();
        } catch (Exception exception) {
            throw new ApiException("Exception occurred while trying to get all the cameras", exception);
        }
    }

}
