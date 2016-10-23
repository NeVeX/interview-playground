package com.mark.redbubble.api.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
@Root(strict = false) // We are not going to de/serialize everything
class Work {

    @Element(required = false)
    private Long id;
    @Element(required = false)
    private Exif exif;
    @ElementList(required = false)
    private List<Url> urls;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exif getExif() {
        return exif;
    }

    public void setExif(Exif exif) {
        this.exif = exif;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }
}
