package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import com.mark.redbubble.output.HtmlFileWriter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWebsiteGenerator {

    private final Set<CameraInformation> originalCameras;
    private final HtmlFileWriter htmlFileWriter;

    public CameraWebsiteGenerator(Set<CameraInformation> cameras, HtmlFileWriter htmlFileWriter) {
        if ( cameras == null || cameras.isEmpty()) { throw new IllegalArgumentException("Provided cameras is not valid"); }
        if ( htmlFileWriter == null) { throw new IllegalArgumentException("Provided htmlFileWriter is null"); }

        this.originalCameras = cameras;
        this.htmlFileWriter = htmlFileWriter;
    }

    public void testing() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML5");

        resolver.setSuffix(".html");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);


        Context context = new Context();

        String name = "John Doe";
        context.setVariable("name", name);
        String html = templateEngine.process("templates/index", context);
        try {
            htmlFileWriter.writeHtml("mark-testing"+ UUID.randomUUID().toString(), html);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}
