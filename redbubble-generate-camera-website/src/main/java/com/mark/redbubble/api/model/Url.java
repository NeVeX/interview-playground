package com.mark.redbubble.api.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
@Root(strict = false)
public class Url {

    @Text(required = false)
    private String url;
    @Attribute(required = false)
    private String type; // large/small/medium

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
