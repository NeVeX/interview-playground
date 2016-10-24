package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.output.FileWriterException;
import com.mark.redbubble.output.FileWriter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWebsiteGenerator {

    private final Set<CameraInformation> allCameras;
    private final FileWriter fileWriter;
    private final TemplateEngine templateEngine;
    private final IndexPageGenerator indexPageGenerator;
    private final CameraMakePageGenerator cameraMakePageGenerator;

    public CameraWebsiteGenerator(Set<CameraInformation> allCameras, FileWriter fileWriter) {
        if ( allCameras == null || allCameras.isEmpty()) { throw new IllegalArgumentException("Provided allCameras is not valid"); }
        if ( fileWriter == null) { throw new IllegalArgumentException("Provided fileWriter is null"); }

        this.allCameras = allCameras;
        this.fileWriter = fileWriter;
        this.templateEngine = createTemplateEngine();
        this.indexPageGenerator = new IndexPageGenerator(allCameras, this.templateEngine);
        this.cameraMakePageGenerator = new CameraMakePageGenerator(allCameras, this.templateEngine);
    }

    public void generatePages() throws FileWriterException {

        indexPageGenerator.createIndexPage(fileWriter);
        cameraMakePageGenerator.createCameraMakePages(fileWriter);
    }

    private TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML5");
        resolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }

}
