package com.mark.redbubble.html;

import com.mark.redbubble.model.CameraInformation;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Locale;
import java.util.Set;

/**
 * Created by Mark Cunningham on 10/22/2016.
 */
public class CameraWebsiteGenerator {

    private final Set<CameraInformation> originalCameras;

    public CameraWebsiteGenerator(Set<CameraInformation> cameras) {
        if ( cameras == null || cameras.isEmpty()) { throw new IllegalArgumentException("Provided cameras is not valid"); }
        this.originalCameras = cameras;
    }


    public void testing() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML5");

        resolver.setSuffix(".html");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        final Context context = new Context(Locale.US);
        String name = "John Doe";
        context.setVariable("name", name);
        final String html = templateEngine.process("templates/index", context);
        html.toCharArray();
    }

}
