package com.mark.redbubble.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/23/2016.
 * <br>Utility class that offers simple methods to perform various actions on models
 */
public class ModelUtils {

    private final static String NON_ALLOWABLE_CHARACTERS = "[^a-zA-Z0-9-_\\. ]";

    // Note, this may not get 10 pictures - it depends on the input

    /**
     * Returns at most, 10, random and distinct, pictures from the given camera set
     * @param allCameras - the set of cameras to use
     * @return - the set of picture urls (at most 10)
     */
    public static Set<String> getAtMostTenRandomThumbnails(Set<CameraInformation> allCameras) {
        return getRandomThumbnails(allCameras, 10);
    }

    /**
     * Returns random and distinct, pictures from the given camera set
     * @param allCameras - the set of cameras to use
     * @param limit - the max amount of pictures
     * @return - the set of picture urls
     */
    public static Set<String> getRandomThumbnails(Set<CameraInformation> allCameras, int limit) {
        return allCameras
                .stream()
                .unordered() // allow any order
                .map(CameraInformation::getThumbnailPicture) // use the thumbnail function to get the most appropriate size
                .filter(Optional::isPresent) // remove empty thumbnails
                .map(Optional::get) // get the actual thumbnail
                .map(PictureUrl::getUrl) // get the actual url of the picture
                .limit(limit) // impose the give limit
                .collect(Collectors.toSet());
    }

    /**
     * Given a set of cameras and a function to map with, this will return the map with respect to the input function
     * @param cameras - the set of cameras
     * @param nameMapperFunction - the mapper function to apply on each data entry
     * @return - the new map generated from the input mapper function
     */
    public static Map<String, String> getNameToHtmlFileNameMap(Set<CameraInformation> cameras, Function<CameraInformation, String> nameMapperFunction) {
        return cameras
                .stream()
                .map(nameMapperFunction) // apply the mapper function
                .distinct()
                .collect(Collectors.toMap(ModelUtils::createNiceTitle, ModelUtils::createSafeHtmlFileName));
    }

    /**
     * Given a title, this will return a nice parse title, removing invalid chars for example
     * @param inputName - the raw input to tidy up
     * @return - the nice/tidied title
     */
    public static String createNiceTitle(String inputName) {
        String safeString = inputName.replaceAll(NON_ALLOWABLE_CHARACTERS, "");
        return StringUtils.capitalize(StringUtils.lowerCase(safeString));
    }


    /**
     * Given a html (file) name, this will return an acceptable file name, removing invalid chars for example
     * @param rawHtmlName - the raw html file to parse
     * @return - the valid file name to use
     */
    public static String createSafeHtmlFileName(String rawHtmlName) {
        String safeString = rawHtmlName.replaceAll(NON_ALLOWABLE_CHARACTERS, "");
        return safeString.replaceAll(" ", "_").toLowerCase() + ".html"; // special replace for spaces, and add .html
    }

}
