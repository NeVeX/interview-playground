package com.mark.redbubble.api.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
@Root(strict = false) // We are not going to de/serialize everything
public class CameraWorksApiResponse {

    @ElementList(inline = true) // The root response is directly the start of the list
    private List<Work> works;

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }
}
