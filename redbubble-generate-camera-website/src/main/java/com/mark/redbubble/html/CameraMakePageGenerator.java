package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.model.ModelUtils;
import com.mark.redbubble.output.FileWriter;
import com.mark.redbubble.output.FileWriterException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
class CameraMakePageGenerator {

    private final Map<String, Set<CameraInformation>> cameraMakesToModels;
    private final TemplateEngine templateEngine;

    CameraMakePageGenerator(Set<CameraInformation> allCameras, TemplateEngine templateEngine) {
        this.cameraMakesToModels = getCameraMakesToModels(allCameras);
        this.templateEngine = templateEngine;
    }

    private Map<String, Set<CameraInformation>> getCameraMakesToModels(Set<CameraInformation> allCameras) {
        return allCameras
                .stream()
                .collect(groupingBy(CameraInformation::getCameraMake, Collectors.toSet()));
    }

    void createCameraMakePages(FileWriter fileWriter) throws FileWriterException {
        // TODO: parallelize this
        // Not using streams below so as to avoid wrapping the checked exception into a unchecked exception
        for ( Map.Entry<String, Set<CameraInformation>> makeEntry : cameraMakesToModels.entrySet()) {
            createCameraMakePage(makeEntry, fileWriter);
        }

    }

    private void createCameraMakePage(Map.Entry<String, Set<CameraInformation>> cameraMakeEntry, FileWriter fileWriter) throws FileWriterException {

        String cameraModel = cameraMakeEntry.getKey();
        Set<CameraInformation> allModels = cameraMakeEntry.getValue();

        Map<String, String> allModelsToHtmlNames = ModelUtils.getNameToHtmlFileNameMap(allModels, CameraInformation::getCameraModel);

        Context context = new Context();
        context.setVariable("all_camera_models", allModelsToHtmlNames);
        context.setVariable("highlight_pictures", ModelUtils.getAtMostTenRandomThumbnails(allModels));
        String contents = templateEngine.process("templates/camera_make", context);
        fileWriter.writeContentsToFile(ModelUtils.createSafeHtmlFileName(cameraModel), ".html", contents);

    }


}
