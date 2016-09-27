package com.mark.interview.payroll;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.Assert.fail;

/**
 * Created by Mark Cunningham on 9/24/2016.
 */
public class TestingUtils {

    public static File createRandomCsvFileWithData(String data) {
        File randomFile = createRandomCsvFile();
        writeDataToFile(data, randomFile);
        return randomFile;
    }

    public static File createRandomCsvFile() {
        try {
            File randomFile = File.createTempFile(UUID.randomUUID().toString(), ".csv");
            randomFile.deleteOnExit();
            return randomFile;
        } catch (Exception e ) {
            fail("Could not generate temporary file for testing.\n"+e);
            return null;
        }
    }

    public static void writeDataToFile(String data, File file) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.toURI()))) {
            writer.write(data);
        } catch (Exception e ) {
            fail("Could not save data into file ["+file.getAbsolutePath()+"]\n"+e);
        }
    }

}
