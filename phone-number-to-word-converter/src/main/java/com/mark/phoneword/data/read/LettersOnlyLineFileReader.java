package com.mark.phoneword.data.read;

import com.mark.phoneword.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 9/29/2016.
 * <br>Extension of the {@link FileReader} that supports reading Letters only from files
 * Note - all non letters are ignored in the file read, numbers and punctuation for example
 */
class LettersOnlyLineFileReader extends FileReader<Set<String>> {

    @Override
    protected Set<String> process(BufferedReader br) throws IOException {
        Set<String> readInLetters = br.lines()
            .parallel()
            .filter(StringUtils::isNotBlank)
            // Parse the string and remove invalid characters
            .map( word -> StringUtils.getLettersOnly(word.trim().toLowerCase()))
            .filter(StringUtils::isNotBlank) // parse above can make the string empty, so check again
            .collect(Collectors.toCollection(HashSet::new));
        if ( readInLetters.isEmpty()) {
            return null; // Return null to indicate no data read
        } else {
            return readInLetters;
        }
    }
}
