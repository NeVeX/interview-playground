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
class CameraMakePageGenerator implements Generator {

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

    @Override
    public void generate(FileWriter fileWriter) throws FileWriterException {
        try {
            cameraMakesToModels.entrySet()
                    .parallelStream() // we can do this in parallel, so lets do so
                    .forEach(cameraEntry -> createCameraMakePage(cameraEntry, fileWriter));
        } catch (CameraModelGeneratorRuntimeException runtimeException) {
            throw runtimeException.fileWriterException; // Workaround the problem with Java streams and checked exceptions
        }
    }

    String createContent(String cameraMake, Set<CameraInformation> allModels) {

        Map<String, String> allModelsToHtmlNames = ModelUtils.getNameToHtmlFileNameMap(allModels, CameraInformation::getCameraModel);

        Context context = new Context();
        context.setVariable("camera_make_name", cameraMake);
        context.setVariable("all_camera_models", allModelsToHtmlNames);
        context.setVariable("highlight_pictures", ModelUtils.getAtMostTenRandomThumbnails(allModels));
        return templateEngine.process("templates/camera_make", context);

    }

    private void createCameraMakePage(Map.Entry<String, Set<CameraInformation>> cameraMakeEntry, FileWriter fileWriter) throws CameraModelGeneratorRuntimeException {
        String content = createContent(cameraMakeEntry.getKey(), cameraMakeEntry.getValue());
        String safeHtmlFileName = ModelUtils.createSafeHtmlFileName(cameraMakeEntry.getKey());
        try {
            fileWriter.writeContentsToFile(safeHtmlFileName, content);
        } catch (FileWriterException fileWriterException) {
            throw new CameraModelGeneratorRuntimeException(fileWriterException);
        }
    }

    private static class CameraModelGeneratorRuntimeException extends RuntimeException {
        private final FileWriterException fileWriterException;

        CameraModelGeneratorRuntimeException(FileWriterException fileWriterException) {
            this.fileWriterException = fileWriterException;
        }
    }

}
