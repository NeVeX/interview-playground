package com.mark.phoneword.data.read;

import java.util.Set;

/**
 * Created by Mark Cunningham on 9/30/2016.
 */
public final class FileReaderFactory {

    public static FileReader<Set<String>> lettersOnlyLineReader() {
        return Holder.LETTERS_ONLY_LINE_FILE_READER;
    }

    public static FileReader<Set<Long>> longsOnlyLineReader() {
        return Holder.LONGS_ONLY_LINE_FILE_READER;
    }

    private FileReaderFactory() { }

    private static class Holder {

        private final static FileReader<Set<String>> LETTERS_ONLY_LINE_FILE_READER = new LettersOnlyLineFileReader();
        private final static FileReader<Set<Long>> LONGS_ONLY_LINE_FILE_READER = new LongsOnlyLineFileReader();

    }

}
