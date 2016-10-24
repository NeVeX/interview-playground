package com.mark.redbubble.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/24/2016.
 */
public class ModelUtilsTest {

    @Test
    public void assertTitleStringCreationIsAsExpected() {

        String convertedTitle = ModelUtils.createNiceTitle("`=[]\\';/,~!@#$%^&*()+}|{\":?><");
        assertThat(convertedTitle).isEmpty();
        convertedTitle = ModelUtils.createNiceTitle("mark ._-abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"); // allowed characters
        assertThat(convertedTitle).isEqualTo("Mark ._-abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz1234567890");
    }

}
