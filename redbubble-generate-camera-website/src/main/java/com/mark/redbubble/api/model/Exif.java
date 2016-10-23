package com.mark.redbubble.api.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
@Root(strict = false) // We are not going to de/serialize everything
public class Exif {

    @Element(required = false)
    private String model;
    @Element(required = false)
    private String make;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }
}
