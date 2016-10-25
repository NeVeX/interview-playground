package com.mark.redbubble.html;

import com.mark.redbubble.output.FileWriter;
import com.mark.redbubble.output.FileWriterException;

/**
 * Created by Mark Cunningham on 10/23/2016.
 * <br>Interface that specifies supported generation of resources, be it webpages, images, scripts
 */
interface Generator {

    /**
     * Starts the generation of the underlying class. All output is directed to the given {@link FileWriter}
     * @param fileWriter - the valid file writer to use
     * @throws FileWriterException - if anything went wrong while writing to the resource
     */
    void generate(FileWriter fileWriter) throws FileWriterException;

}
