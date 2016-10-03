package com.mark.flowershop.input;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mark Cunningham on 10/3/2016.
 */
public class InputProcessorTest {

    private InputProcessor inputProcessor = new InputProcessor();

    @Test
    public void assertValidCommandLineInputWorks() {
        String input = "10 R12";
        InputProcessor.InputOrderParsedResult result = inputProcessor.isInputOrderValid(input);
        assertThat(result.isValid).isTrue();
        assertThat(result.invalidReason).isNullOrEmpty();
        assertThat(result.orderSize).isEqualTo(10);
        assertThat(result.productCode).isEqualTo("R12");
    }

    @Test
    public void assertInvalidNumberIsIdentified() {
        String input = "abc R12";
        InputProcessor.InputOrderParsedResult result = inputProcessor.isInputOrderValid(input);
        assertThat(result.isValid).isFalse();
        assertThat(result.invalidReason).contains("not a number");
    }

    @Test
    public void assertInvalidFormatIsIdentified() {
        String input = "10R12"; // no space
        InputProcessor.InputOrderParsedResult result = inputProcessor.isInputOrderValid(input);
        assertThat(result.isValid).isFalse();
        assertThat(result.invalidReason).contains("format");
    }

    @Test
    public void assertInvalidProductCodeIsIdentified() {
        String input = "10 "; // no product code
        InputProcessor.InputOrderParsedResult result = inputProcessor.isInputOrderValid(input);
        assertThat(result.isValid).isFalse();
        assertThat(result.invalidReason).contains("format");
    }

    @Test
    public void assertNonExistenceProductCodeIsIdentified() {
        String input = "10 DOES_NOT_EXIST"; // no product code exists
        InputProcessor.InputOrderParsedResult result = inputProcessor.isInputOrderValid(input);
        assertThat(result.isValid).isFalse();
        assertThat(result.invalidReason).contains("exists");
    }

    @Test
    public void assertTooManyInputsIsNotAccepted() {
        String input = "10 R12 10 R12"; // only can have one input
        InputProcessor.InputOrderParsedResult result = inputProcessor.isInputOrderValid(input);
        assertThat(result.isValid).isFalse();
        assertThat(result.invalidReason).contains("format");
    }


}
