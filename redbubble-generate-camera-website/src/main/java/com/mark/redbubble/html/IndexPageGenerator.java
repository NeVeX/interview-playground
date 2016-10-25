package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.model.ModelUtils;
import com.mark.redbubble.output.OutputWriter;
import com.mark.redbubble.output.OutputWriterException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mark Cunningham on 10/22/2016.
 * <br>This generator is responsible for creating the index.html page
 */
class IndexPageGenerator implements Generator {

    private final Set<String> pictureHighlights;
    private final Map<String, String> cameraMakes;
    private final TemplateEngine templateEngine;

    /**
     * @param allCameras - all the cameras to consider for the index page
     * @param templateEngine - the template engine to use for creating html content
     */
    IndexPageGenerator(Set<CameraInformation> allCameras, TemplateEngine templateEngine) {
        this.pictureHighlights = ModelUtils.getAtMostTenRandomThumbnails(allCameras);
        this.cameraMakes = ModelUtils.getNameToHtmlFileNameMap(allCameras, CameraInformation::getCameraMake);
        this.templateEngine = templateEngine;
    }

    /**
     * Initiates a new generation of the index.html file using the given data already
     * @param outputWriter - the valid file writer to use
     * @throws OutputWriterException - if something when wrong writing files
     */
    @Override
    public void generate(OutputWriter outputWriter) throws OutputWriterException {
        outputWriter.writeContentsToFile("index.html", createContent());
    }

    /**
     * Creates the content of the HTML using the instance data
     * @return
     */
    String createContent() {
        Context context = new Context();
        context.setVariable("all_camera_makes", cameraMakes);
        context.setVariable("highlight_pictures", pictureHighlights);
        return templateEngine.process("templates/index", context);
    }

}
