package com.mark.redbubble.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class PictureUrl {

    public enum PictureUrlSize {
        Small, Medium, Large, Unknown;

        public static PictureUrlSize toEnum(String enumAsString) {
            if (StringUtils.isNotBlank(enumAsString)) {
                Optional<PictureUrlSize> foundEnum = Arrays.stream(PictureUrlSize.values())
                        .filter(pictureSize -> StringUtils.equalsIgnoreCase(pictureSize.name(), enumAsString))
                        .findFirst();
                if ( foundEnum.isPresent()) {
                    return foundEnum.get();
                }
            }
            return Unknown;
        }

    }

    private final String url;
    private final PictureUrlSize pictureUrlSize;

    public PictureUrl(String url, PictureUrlSize urlSize) {
        this.url = url;
        this.pictureUrlSize = urlSize;
    }

    public String getUrl() {
        return url;
    }

    public PictureUrlSize getPictureUrlSize() {
        return pictureUrlSize;
    }
}
