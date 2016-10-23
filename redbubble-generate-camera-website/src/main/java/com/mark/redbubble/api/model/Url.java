package com.mark.redbubble.api.model;

import org.simpleframework.xml.*;

import java.util.List;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
@Root(strict = false)
class Url {

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
