package com.mark.redbubble.html;

import com.mark.redbubble.output.FileWriter;
import com.mark.redbubble.output.FileWriterException;

/**
 * Created by Mark Cunningham on 10/23/2016.
 */
public interface Generator {

    void generate(FileWriter fileWriter) throws FileWriterException;

}
