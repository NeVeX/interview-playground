package com.mark.phoneword.convert;

import java.util.Set;

/**
 * Created by Mark Cunningham on 9/29/2016.
 */
public interface Converter<I, O> {

    Set<O> convert(I input);

}
