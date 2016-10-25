package com.mark.redbubble.html;

import com.mark.redbubble.output.OutputWriterException;

/**
 * Created by Mark Cunningham on 10/25/2016.
 * <br>Internal exception class to help with use of checked exceptions in Java Streams
 */
class StreamGeneratorWrapperException extends RuntimeException {

    private final OutputWriterException outputWriterException;

    StreamGeneratorWrapperException(OutputWriterException outputWriterException) {
        this.outputWriterException = outputWriterException;
    }

    OutputWriterException getOutputWriterException() {
        return outputWriterException;
    }

}
