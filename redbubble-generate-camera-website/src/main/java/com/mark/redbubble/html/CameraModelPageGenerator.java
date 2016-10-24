package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.model.ModelUtils;
import com.mark.redbubble.output.FileWriter;
import com.mark.redbubble.output.FileWriterException;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
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
        // TODO: parallelize this
        // Not using streams below so as to avoid wrapping the checked exception into a unchecked exception
        for ( Map.Entry<String, Set<CameraInformation>> makeEntry : cameraModels.entrySet()) {
            createCameraMakePage(makeEntry, fileWriter);
        }

    }

    private void createCameraMakePage(Map.Entry<String, Set<CameraInformation>> cameraModels, FileWriter fileWriter) throws FileWriterException {

        String cameraModelName = cameraModels.getKey();
        Set<CameraInformation> allModels = cameraModels.getValue();
        CameraInformation exampleCameraInfo = allModels.stream().findAny().get();

        Context context = new Context();

        context.setVariable("camera_model_name", cameraModelName);
        context.setVariable("camera_make_name", exampleCameraInfo.getCameraMake());
        context.setVariable("camera_make_html", ModelUtils.createSafeHtmlFileName(exampleCameraInfo.getCameraMake()));

        context.setVariable("highlight_pictures", ModelUtils.getRandomThumbnails(allModels, HIGHLIGHT_PICTURE_AMOUNT_MAX));
        String contents = templateEngine.process("templates/camera_model", context);

        String safeHtmlFileName = ModelUtils.createSafeHtmlFileName(cameraModelName);
        fileWriter.writeContentsToFile(safeHtmlFileName, contents);

    }
}
