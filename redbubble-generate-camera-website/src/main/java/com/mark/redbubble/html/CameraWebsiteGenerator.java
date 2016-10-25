package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.output.FileWriterException;
import com.mark.redbubble.output.FileWriter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWebsiteGenerator implements Generator {

    private final List<Generator> allGenerators = new ArrayList<>();

    public CameraWebsiteGenerator(Set<CameraInformation> allCameras) {
        if ( allCameras == null || allCameras.isEmpty()) { throw new IllegalArgumentException("Provided allCameras is not valid"); }

        TemplateEngine templateEngine = createTemplateEngine();
        Generator indexPageGenerator = new IndexPageGenerator(allCameras, templateEngine);
        Generator cameraMakePageGenerator = new CameraMakePageGenerator(allCameras, templateEngine);
        Generator cameraModelPageGenerator = new CameraModelPageGenerator(allCameras, templateEngine);
        Generator staticResourcesGenerator = new StaticResourcesGenerator();

        allGenerators.add(indexPageGenerator);
        allGenerators.add(cameraMakePageGenerator);
        allGenerators.add(cameraModelPageGenerator);
        allGenerators.add(staticResourcesGenerator);
    }

    @Override
    public void generate(FileWriter fileWriter) throws FileWriterException {
        for ( Generator generator : allGenerators) {
            generator.generate(fileWriter);
        }
    }

    TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML5");
        resolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }

}
