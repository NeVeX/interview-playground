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
class CameraModelPageGenerator implements Generator {

    private final Map<String, Set<CameraInformation>> cameraModels;
    private final TemplateEngine templateEngine;
    private final static int HIGHLIGHT_PICTURE_AMOUNT_MAX = 100;

    CameraModelPageGenerator(Set<CameraInformation> allCameras, TemplateEngine templateEngine) {
        this.cameraModels = getCameraModels(allCameras);
        this.templateEngine = templateEngine;
    }

    private Map<String, Set<CameraInformation>> getCameraModels(Set<CameraInformation> allCameras) {
        return allCameras
                .stream()
                .collect(groupingBy(CameraInformation::getCameraModel, Collectors.toSet()));
    }

    @Override
    public void generate(FileWriter fileWriter) throws FileWriterException {
        try {
            cameraModels.entrySet()
                    .parallelStream() // we can do this in parallel, so lets do so
                    .forEach(cameraEntry -> createCameraModelPage(cameraEntry, fileWriter));
        } catch (CameraMakeGeneratorRuntimeException runtimeException) {
            throw runtimeException.fileWriterException; // Workaround the problem with Java streams and checked exceptions
        }
    }

    private void createCameraModelPage(Map.Entry<String, Set<CameraInformation>> cameraModels, FileWriter fileWriter) throws CameraMakeGeneratorRuntimeException {
        String contents = createContent(cameraModels.getKey(), cameraModels.getValue());
        String safeHtmlFileName = ModelUtils.createSafeHtmlFileName(cameraModels.getKey());
        try {
            fileWriter.writeContentsToFile(safeHtmlFileName, contents);
        } catch (FileWriterException fileWriterException) {
            throw new CameraMakeGeneratorRuntimeException(fileWriterException);
        }
    }

    String createContent(String cameraModelName, Set<CameraInformation> allModels) {

        CameraInformation exampleCameraInfo = allModels.stream().findAny().get();

        Context context = new Context();

        context.setVariable("camera_model_name", cameraModelName);
        context.setVariable("camera_make_name", exampleCameraInfo.getCameraMake());
        context.setVariable("camera_make_html", ModelUtils.createSafeHtmlFileName(exampleCameraInfo.getCameraMake()));

        context.setVariable("highlight_pictures", ModelUtils.getRandomThumbnails(allModels, HIGHLIGHT_PICTURE_AMOUNT_MAX));
        return templateEngine.process("templates/camera_model", context);
    }

    private static class CameraMakeGeneratorRuntimeException extends RuntimeException {
        private final FileWriterException fileWriterException;

        CameraMakeGeneratorRuntimeException(FileWriterException fileWriterException) {
            this.fileWriterException = fileWriterException;
        }
    }

}
