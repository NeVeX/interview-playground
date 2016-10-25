package com.mark.redbubble.html;

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
 * <br>This generator is responsible for creating all the Make camera pages.
 */
class CameraMakePageGenerator implements Generator {

    private final Map<String, Set<CameraInformation>> cameraMakesToModels;
    private final TemplateEngine templateEngine;

    /**
     * @param allCameras - all the cameras to use for building make pages
     * @param templateEngine - the template engine to use when creating content
     */
    CameraMakePageGenerator(Set<CameraInformation> allCameras, TemplateEngine templateEngine) {
        this.cameraMakesToModels = getCameraMakesToModels(allCameras);
        this.templateEngine = templateEngine;
    }

    /**
     * Helper method to get the mapping of Make -> All Make Models
     */
    private Map<String, Set<CameraInformation>> getCameraMakesToModels(Set<CameraInformation> allCameras) {
        return allCameras
                .stream()
                .collect(groupingBy(CameraInformation::getCameraMake, Collectors.toSet()));
    }

    /**
     * Invoking this will generate all the make pages using the already provided camera set
     * @param outputWriter - the valid file writer to use
     * @throws OutputWriterException
     */
    @Override
    public void generate(OutputWriter outputWriter) throws OutputWriterException {
        try {
            cameraMakesToModels.entrySet()
                    .parallelStream() // we can do this in parallel, so lets do so
                    .forEach(cameraEntry -> createCameraMakePage(cameraEntry, outputWriter));
        } catch (StreamGeneratorWrapperException runtimeException) {
            throw runtimeException.getOutputWriterException(); // Workaround the problem with Java streams and checked exceptions
        }
    }

    /**
     * Given an instance of the camera make and it's models, create the content for that html page
     * @param cameraMake - the camera make of this page
     * @param allModels - the set of models that roll into this make
     * @return - the html content
     */
    String createContent(String cameraMake, Set<CameraInformation> allModels) {
        // Get the data list of all the models -> model.html files
        Map<String, String> allModelsToHtmlNames = ModelUtils.getNameToHtmlFileNameMap(allModels, CameraInformation::getCameraModel);
        Context context = new Context();
        // Add context information for the template engine
        context.setVariable("camera_make_name", cameraMake);
        context.setVariable("all_camera_models", allModelsToHtmlNames);
        context.setVariable("highlight_pictures", ModelUtils.getAtMostTenRandomThumbnails(allModels));
        // Use the template engine and the template html file to create the new content
        return templateEngine.process("templates/camera_make", context);
    }

    /**
     * Given an instance of the camera make and it's models, write the data to the given {@link OutputWriter}
     * @param cameraMakeEntry - this entry of a camera make
     * @param outputWriter - the file writer to use for writing the html files to
     * @throws StreamGeneratorWrapperException - the runtime exception if something goes wrong while writing data
     */
    private void createCameraMakePage(Map.Entry<String, Set<CameraInformation>> cameraMakeEntry, OutputWriter outputWriter) throws StreamGeneratorWrapperException {
        String content = createContent(cameraMakeEntry.getKey(), cameraMakeEntry.getValue());
        String safeHtmlFileName = ModelUtils.createSafeHtmlFileName(cameraMakeEntry.getKey());
        try {
            outputWriter.writeContentsToFile(safeHtmlFileName, content);
        } catch (OutputWriterException outputWriterException) {
            throw new StreamGeneratorWrapperException(outputWriterException);
        }
    }

}
