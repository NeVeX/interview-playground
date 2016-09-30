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
class LongsOnlyLineFileReader extends FileReader<Set<Long>> {

    @Override
    protected Set<Long> process(BufferedReader br) throws IOException {
        Set<Long> readInLongs = br.lines()
            .parallel()
            .filter(StringUtils::isNotBlank)
            .map( word -> StringUtils.getNumbersOnly(word.trim().toLowerCase()))
            .filter(StringUtils::isNotBlank) // parse above can make the string empty
            .map(Long::valueOf)
            .collect(Collectors.toCollection(HashSet::new));
        if ( readInLongs.isEmpty()) {
            return null; // Return null to indicate no data read
        } else {
            return readInLongs;
        }
    }
}
