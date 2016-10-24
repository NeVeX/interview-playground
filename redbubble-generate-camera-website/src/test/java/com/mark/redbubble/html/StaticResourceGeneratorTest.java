package com.mark.redbubble.html;

import com.mark.redbubble.output.FileWriter;
import com.mark.redbubble.output.FileWriterException;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Created by Mark Cunningham on 10/23/2016.
 */
public class StaticResourceGeneratorTest {

    @Test
    public void assertStaticResourcesAreExported() throws GeneratorException, FileWriterException {
        new StaticResourcesGenerator().createResources(new FileWriter(Paths.get("C:\\Temp\\")));
    }

}
