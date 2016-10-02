package com.mark.phoneword.convert;

import java.util.Set;

/**
 * Created by Mark Cunningham on 9/29/2016.
 * <br>Converter interface that defines the conversion to/from types with simple method to invoke the conversion.
 * @param <I> - The input type
 * @param <O> - The output type
 */
public interface Converter<I, O> {

    /**
     * Given the input, this method will attempt conversions to the defined output type
     * @param input - Non null input type
     * @return - The set of converted data
     */
    Set<O> convert(I input);

}
