package com.mark.redbubble.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class PictureUrl {

    public enum PictureUrlSize {
        Small(1), Medium(2), Large(3), Unknown(4);

        int sizeOrder;

        PictureUrlSize(int sizeOrder) {
            this.sizeOrder = sizeOrder;
        }

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

    public static Comparator<PictureUrl> comparatorForOrderSize() {
        return (o1, o2) -> Integer.compare(o1.pictureUrlSize.sizeOrder, o2.pictureUrlSize.sizeOrder);
    }

    public String getUrl() {
        return url;
    }

    public PictureUrlSize getPictureUrlSize() {
        return pictureUrlSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PictureUrl that = (PictureUrl) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
