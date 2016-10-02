package com.mark.phoneword.data.read;

import java.util.Set;

/**
 * Created by Mark Cunningham on 9/30/2016.
 * <br> FileReader Factory that offers simple methods to get instances of various types of file readers
 */
public final class FileReaderFactory {

    /**
     * @return - A dictionary, one word only per line file reader (all words with numbers/punctuations are ignored)
     */
    public static FileReader<Set<String>> dictionaryLineReader() {
        return Holder.DICTIONARY_LINE_FILE_READER;
    }

    /**
     * @return - A numbers (Long) only per line file reader (all numbers/punctuations are ignored)
     */
    public static FileReader<Set<Long>> longsOnlyLineReader() {
        return Holder.LONGS_ONLY_LINE_FILE_READER;
    }

    private FileReaderFactory() { }

    /**
     * Inner class to hold the default instances (this is lazy loaded and thread safe also)
     */
    private static class Holder {

        private final static FileReader<Set<String>> DICTIONARY_LINE_FILE_READER = new DictionaryLineFileReader();
        private final static FileReader<Set<Long>> LONGS_ONLY_LINE_FILE_READER = new LongsOnlyLineFileReader();

    }

}
