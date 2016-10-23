package com.mark.redbubble.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraInformation {

    private final long cameraId;
    private final String cameraMake;
    private final String cameraModel;
    private Set<PictureUrl> pictureUrls;

    private CameraInformation(Builder builder) {
        this.cameraId = builder.cameraId;
        this.cameraMake = builder.cameraMake;
        this.cameraModel = builder.cameraModel;
        pictureUrls = Collections.unmodifiableSet(builder.pictureUrls);
    }

    public long getCameraId() {
        return cameraId;
    }

    public String getCameraMake() {
        return cameraMake;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public Set<PictureUrl> getPictureUrls() {
        return pictureUrls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CameraInformation that = (CameraInformation) o;
        return cameraId == that.cameraId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cameraId);
    }


    public static Builder builder(long cameraId) {
        return new Builder(cameraId);
    }

    public static class Builder {
        private long cameraId;
        private String cameraMake;
        private String cameraModel;
        private Set<PictureUrl> pictureUrls = new HashSet<>();

        private Builder(long cameraId) {
            this.cameraId = cameraId;
        }

        public Builder withCameraMake(String cameraMake) {
            this.cameraMake = cameraMake;
            return this;
        }

        public Builder withCameraModel(String cameraModel) {
            this.cameraModel = cameraModel;
            return this;
        }

        public Builder addPictureUrl(PictureUrl pictureUrl) {
            this.pictureUrls.add(pictureUrl);
            return this;
        }

        public Builder addPictureUrls(Set<PictureUrl> pictureUrls) {
            this.pictureUrls.addAll(pictureUrls);
            return this;
        }

        public CameraInformation build() throws IllegalStateException {
            CameraInformation cameraInformation = new CameraInformation(this);
            if (StringUtils.isBlank(cameraInformation.cameraMake)) { throw new IllegalStateException("Camera Make cannot be blank"); }
            if (StringUtils.isBlank(cameraInformation.cameraModel)) { throw new IllegalStateException("Camera Model cannot be blank"); }
            return cameraInformation;
        }
    }


}
