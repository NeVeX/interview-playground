package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.model.ModelUtils;
import com.mark.redbubble.output.FileWriter;
import com.mark.redbubble.output.FileWriterException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
class IndexPageGenerator implements Generator {

    private final Set<String> pictureHighlights;
    private final Map<String, String> cameraMakes;
    private final TemplateEngine templateEngine;

    IndexPageGenerator(Set<CameraInformation> allCameras, TemplateEngine templateEngine) {
        this.pictureHighlights = ModelUtils.getAtMostTenRandomThumbnails(allCameras);
        this.cameraMakes = ModelUtils.getNameToHtmlFileNameMap(allCameras, CameraInformation::getCameraMake);
        this.templateEngine = templateEngine;
    }

    @Override
    public void generate(FileWriter fileWriter) throws FileWriterException {
        Context context = new Context();
        context.setVariable("all_camera_makes", cameraMakes);
        context.setVariable("highlight_pictures", pictureHighlights);
        String contents = templateEngine.process("templates/index", context);
        fileWriter.writeContentsToFile("index.html", contents);
    }



}
