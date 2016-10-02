package com.mark.phoneword.data.read;

import com.mark.phoneword.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 9/29/2016.
 * <br>Extension of the {@link FileReader} that supports reading dictionary words from files.
 * Note - Any word with non-characters are ignored in the file read.
 */
class DictionaryLineFileReader extends FileReader<Set<String>> {

    @Override
    protected Set<String> process(BufferedReader br) throws IOException {
        Set<String> readInLetters = br.lines()
            .parallel()
            .filter(StringUtils::isNotBlank)
            // Filter out any lines that have characters removed (since the word is now invalid)
            .filter(nonEmptyLine -> StringUtils.getLettersOnly(nonEmptyLine).length() == nonEmptyLine.length())
            .collect(Collectors.toCollection(HashSet::new));
        if ( readInLetters.isEmpty()) {
            return null; // Return null to indicate no data read
        } else {
            return readInLetters;
        }
    }
}
