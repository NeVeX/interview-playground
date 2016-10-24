package com.mark.redbubble.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/23/2016.
 */
public class ModelUtils {

    private final static String NON_ALPHA_NUMERIC_REGEX = "[^a-zA-Z0-9 ]+";

    // Note, this may not get 10 pictures - it depends on the input
    public static Set<String> getAtMostTenRandomThumbnails(Set<CameraInformation> allCameras) {
        return getRandomThumbnails(allCameras, 10);
    }

    public static Set<String> getRandomThumbnails(Set<CameraInformation> allCameras, int limit) {
        return allCameras
                .stream()
                .unordered()
                .map(CameraInformation::getThumbnailPicture)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PictureUrl::getUrl)
                .limit(limit)
                .collect(Collectors.toSet());
    }

    public static Map<String, String> getNameToHtmlFileNameMap(Set<CameraInformation> cameras, Function<CameraInformation, String> nameMapperFunction) {
        return cameras
                .stream()
                .map(nameMapperFunction)
                .distinct()
                .collect(Collectors.toMap(ModelUtils::createStringTitle, ModelUtils::createSafeHtmlFileName));
    }

    public static String createStringTitle(String inputName) {
        String safeString = inputName.replaceAll(NON_ALPHA_NUMERIC_REGEX, "");
        return StringUtils.capitalize(StringUtils.lowerCase(safeString));
    }

    public static String createSafeHtmlFileName(String rawHtmlName) {
        String safeString = rawHtmlName.replaceAll(NON_ALPHA_NUMERIC_REGEX, "");
        return safeString.replaceAll(" ", "_").toLowerCase() + ".html"; // special replace for spaces, and add .html
    }

}
