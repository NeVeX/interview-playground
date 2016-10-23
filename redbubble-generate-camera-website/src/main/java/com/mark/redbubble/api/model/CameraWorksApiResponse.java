package com.mark.redbubble.api.model;

import com.mark.redbubble.api.ConversionException;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
@Root(strict = false) // We are not going to de/serialize everything
public class CameraWorksApiResponse {

    @ElementList(inline = true, required = false) // The root response is directly the start of the list
    private List<Work> works;

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    public static CameraWorksApiResponse convertFromStream(InputStream inputStream) throws ConversionException {
        try {
            return new Persister().read(CameraWorksApiResponse.class, inputStream);
        } catch (Exception exception ) {
            throw new ConversionException("Could not convert input stream to "+CameraWorksApiResponse.class.getName(), exception);
        }
    }
}
