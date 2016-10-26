package com.mark.redbubble.output.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.model.ModelUtils;
import com.mark.redbubble.output.OutputWriter;
import com.mark.redbubble.output.OutputWriterException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by Mark Cunningham on 10/22/2016.
 * <br>This generator is responsible for creation of camera model classes
 */
class CameraModelPageGenerator implements Generator {

    private final Map<String, Set<CameraInformation>> cameraModels;
    private final TemplateEngine templateEngine;
    private final static int HIGHLIGHT_PICTURE_AMOUNT_MAX = 100;

    /**
     * @param allCameras - all the cameras to consider during generation
     * @param templateEngine - the template engine to use for html content creation
     */
    CameraModelPageGenerator(Set<CameraInformation> allCameras, TemplateEngine templateEngine) {
        this.cameraModels = getCameraModels(allCameras);
        this.templateEngine = templateEngine;
    }

    /**
     * Helper method to get a Map of all Models to similar models
     */
    private Map<String, Set<CameraInformation>> getCameraModels(Set<CameraInformation> allCameras) {
        return allCameras
                .parallelStream()
                .collect(groupingBy(CameraInformation::getCameraModel, Collectors.toSet()));
    }

    /**
     * Initiates a new write generation of the model files and contents using the given file writer
     * @param outputWriter - the valid file writer to use
     * @throws OutputWriterException
     */
    @Override
    public void generate(OutputWriter outputWriter) throws OutputWriterException {
        try {
            cameraModels.entrySet()
                    .parallelStream() // we can do this in parallel, so lets do so
                    .forEach(cameraEntry -> createCameraModelPage(cameraEntry, outputWriter));
        } catch (StreamGeneratorWrapperException runtimeException) {
            throw runtimeException.getOutputWriterException(); // Workaround the problem with Java streams and checked exceptions
        }
    }

    /**
     * Given an instance of the camera models and same models, write the data to the given {@link OutputWriter}
     * @param cameraModels - this entry of a camera model
     * @param outputWriter - the file writer to use for writing the html files to
     * @throws StreamGeneratorWrapperException - the runtime exception if something goes wrong while writing data
     */
    private void createCameraModelPage(Map.Entry<String, Set<CameraInformation>> cameraModels, OutputWriter outputWriter) throws StreamGeneratorWrapperException {
        String contents = createContent(cameraModels.getKey(), cameraModels.getValue());
        String safeHtmlFileName = ModelUtils.createSafeHtmlFileName(cameraModels.getKey());
        try {
            outputWriter.writeContentsToFile(safeHtmlFileName, contents);
        } catch (OutputWriterException outputWriterException) {
            throw new StreamGeneratorWrapperException(outputWriterException);
        }
    }

    /**
     * Given an instance of the camera model and all the other similar models, create the content for that html page
     * @param cameraModelName - the camera model of this page
     * @param allModels - the set of models that are the same as this make
     * @return - the html content
     */
    String createContent(String cameraModelName, Set<CameraInformation> allModels) {
        // We just need to find one camera model to use as information in the context
        CameraInformation exampleCameraInfo = allModels
                .stream()
                .findAny()
                .get();
        Context context = new Context();
        // Set variables into the context for the template engine to use
        context.setVariable("camera_model_name", ModelUtils.createNiceTitle(cameraModelName));
        context.setVariable("camera_make_name", ModelUtils.createNiceTitle(exampleCameraInfo.getCameraMake()));
        context.setVariable("camera_make_html", ModelUtils.createSafeHtmlFileName(exampleCameraInfo.getCameraMake()));
        // Add as many thumbnails to the context as this model has
        context.setVariable("highlight_pictures", ModelUtils.getRandomThumbnails(allModels, HIGHLIGHT_PICTURE_AMOUNT_MAX));
        // Use the template engine and the template html file to create the new content
        return templateEngine.process("templates/camera_model", context);
    }

}
