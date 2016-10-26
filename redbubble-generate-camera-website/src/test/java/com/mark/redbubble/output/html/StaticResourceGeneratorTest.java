package com.mark.redbubble.output.html;

import com.mark.redbubble.output.OutputWriter;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/23/2016.
 */
public class StaticResourceGeneratorTest {

    @Test
    public void assertStaticResourcesAreExported() throws Exception {

        Path tempDirectory = Files.createTempDirectory(UUID.randomUUID().toString());
        File tempDirectoryAsFile = tempDirectory.toFile();

        OutputWriter outputWriter = new OutputWriter(tempDirectory);

        StaticResourcesGenerator staticResourcesGenerator = new StaticResourcesGenerator();
        staticResourcesGenerator.generate(outputWriter);

        // We expect just the resources to be exported here
        // Make sure the styles has the correct files
        File stylesDirectory = new File(tempDirectoryAsFile, "styles");
        assertThat(stylesDirectory.listFiles().length).isEqualTo(1);
        assertThat(stylesDirectory.list()).contains("main.css");

        File scriptDirectory = new File(tempDirectoryAsFile, "scripts");
        assertThat(scriptDirectory.listFiles().length).isEqualTo(2);
        assertThat(scriptDirectory.list()).contains("jquery-3.1.1.min.js");
        assertThat(scriptDirectory.list()).contains("selection_control.js");


    }

}
