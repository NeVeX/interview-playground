package com.mark.phoneword;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.fail;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class TestingUtils {

    public static File createRandomFileWithData(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        lines.stream().forEach(line -> sb.append(line).append(System.lineSeparator()));
        return createRandomFileWithData(sb.toString());
    }

    public static File createRandomFileWithData(String data) {
        File randomFile = createRandomFile();
        writeDataToFile(data, randomFile);
        return randomFile;
    }

    static File createRandomFile() {
        try {
            File randomFile = File.createTempFile(UUID.randomUUID().toString(), ".random");
            randomFile.deleteOnExit(); // delete the file on exit for cleaniness
            return randomFile;
        } catch (Exception e ) {
            fail("Could not generate temporary file for testing.\n"+e);
            return null;
        }
    }

    static void writeDataToFile(String data, File file) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.toURI()))) {
            writer.write(data);
        } catch (Exception e ) {
            fail("Could not save data into file ["+file.getAbsolutePath()+"]\n"+e);
        }
    }

}
