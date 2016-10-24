package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.output.FileWriterException;
import com.mark.redbubble.output.FileWriter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Set;


/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWebsiteGenerator implements Generator {

    private final IndexPageGenerator indexPageGenerator;
    private final CameraMakePageGenerator cameraMakePageGenerator;
    private final StaticResourcesGenerator staticResourcesGenerator = new StaticResourcesGenerator();

    public CameraWebsiteGenerator(Set<CameraInformation> allCameras) {
        if ( allCameras == null || allCameras.isEmpty()) { throw new IllegalArgumentException("Provided allCameras is not valid"); }

        TemplateEngine templateEngine = createTemplateEngine();
        this.indexPageGenerator = new IndexPageGenerator(allCameras, templateEngine);
        this.cameraMakePageGenerator = new CameraMakePageGenerator(allCameras, templateEngine);
    }

    @Override
    public void generate(FileWriter fileWriter) throws FileWriterException {

        indexPageGenerator.generate(fileWriter);
        cameraMakePageGenerator.generate(fileWriter);
        staticResourcesGenerator.generate(fileWriter);

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
