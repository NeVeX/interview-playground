package com.mark.phoneword;

import com.mark.phoneword.dictionary.DictionaryFactory;
import com.mark.phoneword.dictionary.Dictionary;

/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public class PhoneToWordApplication {

    public static void main(String[] args) {
        // Testing ....

        Dictionary dictionary = DictionaryFactory.getDefault();
        dictionary.isWord("mark");
        dictionary.isWord("train");

    }

}
