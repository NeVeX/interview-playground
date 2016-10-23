package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.model.PictureUrl;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
class IndexPageGenerator {

    private final Set<String> pictureHighlights;
    private final Map<String, String> cameraMakes;
    private final TemplateEngine templateEngine;

    IndexPageGenerator(Set<CameraInformation> allCameras, TemplateEngine templateEngine) {
        this.pictureHighlights = getTopTenRandomPictures(allCameras);
        this.cameraMakes = getCameraMakes(allCameras);
        this.templateEngine = templateEngine;
    }

    private Map<String, String> getCameraMakes(Set<CameraInformation> allCameras) {
        return allCameras
                .stream()
                .map(CameraInformation::getCameraMake)
                .map(StringUtils::lowerCase) // lower case the string for file names (sony.html)
                .collect(Collectors.toMap(string -> string, string -> string+".html"));
    }

    // Note, this may not get 10 pictures - it depends on the input
    private Set<String> getTopTenRandomPictures(Set<CameraInformation> allCameras) {
        return allCameras
                .stream()
                .unordered()
                .map(CameraInformation::getThumbnailPicture)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PictureUrl::getUrl)
                .collect(Collectors.toSet());
    }

    public String createIndexPage() {
        Context context = new Context();
        context.setVariable("all_camera_makes", cameraMakes);
        context.setVariable("highlight_pictures", pictureHighlights);
        return templateEngine.process("templates/index", context);
    }



}
