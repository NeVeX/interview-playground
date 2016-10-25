package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.output.OutputWriter;
import com.mark.redbubble.output.OutputWriterException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Mark Cunningham on 10/22/2016.
 * <br>This generator acts as the parent generator, invoking all child generators that will make up the whole
 * website
 */
public class CameraWebsiteGenerator implements Generator {

    // All generators to use for this website
    private final List<Generator> allGenerators = new ArrayList<>();

    /**
     * @param allCameras - the set of cameras to use for creating the website
     */
    public CameraWebsiteGenerator(Set<CameraInformation> allCameras) {
        if ( allCameras == null || allCameras.isEmpty()) { throw new IllegalArgumentException("Provided allCameras is not valid"); }
        // Create a new template engine
        TemplateEngine templateEngine = createTemplateEngine();
        // Share the camera info and template engine with all generators that need it
        Generator indexPageGenerator = new IndexPageGenerator(allCameras, templateEngine);
        Generator cameraMakePageGenerator = new CameraMakePageGenerator(allCameras, templateEngine);
        Generator cameraModelPageGenerator = new CameraModelPageGenerator(allCameras, templateEngine);
        Generator staticResourcesGenerator = new StaticResourcesGenerator();

        allGenerators.add(indexPageGenerator);
        allGenerators.add(cameraMakePageGenerator);
        allGenerators.add(cameraModelPageGenerator);
        allGenerators.add(staticResourcesGenerator);
    }

    /**
     * Initiates a full generation of a camera website using all given camera data
     * @param outputWriter - the valid file writer to use
     * @throws OutputWriterException
     */
    @Override
    public void generate(OutputWriter outputWriter) throws OutputWriterException {
        try {
            allGenerators
                    .parallelStream()
                    .forEach(generator -> generateWithWrapperException(generator, outputWriter));
        } catch (StreamGeneratorWrapperException wrapperException) {
            throw wrapperException.getOutputWriterException();
        }
    }

    /**
     * Helper method that will be used within a steam, hence will wrap checked exceptions into runtime's
     */
    private void generateWithWrapperException(Generator generator, OutputWriter outputWriter) throws StreamGeneratorWrapperException {
        try {
            generator.generate(outputWriter);
        } catch (OutputWriterException fileWriterExcpetion) {
            throw new StreamGeneratorWrapperException(fileWriterExcpetion);
        }
    }

    /**
     * Creates a new instance of the template engine to use when creating html content
     * @return - the new template engine to use
     */
    TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML5");
        resolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }

}
