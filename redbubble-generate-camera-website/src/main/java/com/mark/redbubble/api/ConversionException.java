package com.mark.redbubble.api;

import com.mark.redbubble.api.model.CameraWorksApiResponse;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class ConversionException extends Exception {

    public ConversionException(String message, Exception exception) {
        super(message, exception);
    }

}
