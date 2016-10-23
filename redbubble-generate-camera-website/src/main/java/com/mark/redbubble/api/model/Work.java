package com.mark.redbubble.api.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
@Root(strict = false) // We are not going to de/serialize everything
public class Work {

    @Element
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
