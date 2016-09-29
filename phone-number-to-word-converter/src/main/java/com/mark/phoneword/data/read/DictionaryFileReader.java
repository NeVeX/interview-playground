package com.mark.phoneword.data.read;

import com.mark.phoneword.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public class DictionaryFileReader extends FileReader<Set<String>> {

    @Override
    protected Set<String> process(BufferedReader br) throws IOException {
        return br
            .lines()
            .parallel()
            .filter(StringUtils::isNotBlank)
            .map( word -> StringUtils.getLettersOnly(word.trim().toLowerCase()))
            .filter(StringUtils::isNotBlank) // parse above can make the string empty
            .collect(Collectors.toCollection(HashSet::new));
    }
}
