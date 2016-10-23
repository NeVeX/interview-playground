package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.output.HtmlFileWriter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWebsiteGenerator {

    private final Set<CameraInformation> allCameras;
    private final HtmlFileWriter htmlFileWriter;
    private final TemplateEngine templateEngine;
    private final IndexPageGenerator indexPageGenerator;

    public CameraWebsiteGenerator(Set<CameraInformation> allCameras, HtmlFileWriter htmlFileWriter) {
        if ( allCameras == null || allCameras.isEmpty()) { throw new IllegalArgumentException("Provided allCameras is not valid"); }
        if ( htmlFileWriter == null) { throw new IllegalArgumentException("Provided htmlFileWriter is null"); }

        this.allCameras = allCameras;
        this.htmlFileWriter = htmlFileWriter;
        this.templateEngine = createTemplateEngine();
        this.indexPageGenerator = new IndexPageGenerator(allCameras, this.templateEngine);
    }

    public void generatePages() {
        String indexPageContents = indexPageGenerator.createIndexPage();
        writeContents("index", indexPageContents);
    }

    private TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML5");
        resolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }

    private Map<String, Set<CameraInformation>> createCameraMakeMap() {
        return allCameras
                .stream()
                .collect(groupingBy(CameraInformation::getCameraMake, Collectors.toSet()));
    }

    private void writeContents(String fileName, String content) {
        try {
            htmlFileWriter.writeHtml("mark-createIndexPage"+ UUID.randomUUID().toString(), content);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
