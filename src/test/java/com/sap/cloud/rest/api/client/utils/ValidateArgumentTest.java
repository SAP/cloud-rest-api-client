package com.sap.cloud.rest.api.client.utils;

import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotEmptyOrNull;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isNotNull;
import static com.sap.cloud.rest.api.client.utils.ValidateArgument.isValidUrl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sap.cloud.rest.api.client.utils.ValidateArgument;

public class ValidateArgumentTest {

    private static final String OBJECT_ARGUMENT = "Object";

    private static final String CHAR_ARRAY_ARGUMENT = "Char array";

    private static final String STRING_ARGUMENT = "String";

    private static final String URL_MSG_PREFIX = "Host URL";

    private static final String TEST_CORRECT_URL = "https://example.com";

    private static final String TEST_INCORRECT_URL = "wrong URL";

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void isNotEmptyOrNullValidStringTest() {
        isNotEmptyOrNull(STRING_ARGUMENT, "string");
    }

    @Test
    public void isNotEmptyOrNullEmptyStringTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(STRING_ARGUMENT + ValidateArgument.CANNOT_BE_BLANK_MSG);

        isNotEmptyOrNull(STRING_ARGUMENT, "");
    }

    @Test
    public void isNotEmptyOrNullNullStringTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(STRING_ARGUMENT + ValidateArgument.CANNOT_BE_BLANK_MSG);

        String str = null;
        isNotEmptyOrNull(STRING_ARGUMENT, str);
    }

    @Test
    public void isNotEmptyOrNullValidCharArrayTest() {
        isNotEmptyOrNull(CHAR_ARRAY_ARGUMENT, "string".toCharArray());
    }

    @Test
    public void isNotEmptyOrNullEmptyCharArrayTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(CHAR_ARRAY_ARGUMENT + ValidateArgument.CANNOT_BE_BLANK_MSG);

        isNotEmptyOrNull(CHAR_ARRAY_ARGUMENT, "".toCharArray());
    }

    @Test
    public void isNotEmptyOrNullNullCharArrayTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(CHAR_ARRAY_ARGUMENT + ValidateArgument.CANNOT_BE_BLANK_MSG);

        char[] charArray = null;
        isNotEmptyOrNull(CHAR_ARRAY_ARGUMENT, charArray);
    }

    @Test
    public void isNotNullValidObjectTest() {
        Object object = new Object();
        isNotNull(OBJECT_ARGUMENT, object);
    }

    @Test
    public void isNotNullNullObjectTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(OBJECT_ARGUMENT + ValidateArgument.CANNOT_BE_NULL_MSG);

        Object object = null;
        isNotNull(OBJECT_ARGUMENT, object);
    }

    @Test
    public void testCorrectUrl() {
        isValidUrl(URL_MSG_PREFIX, TEST_CORRECT_URL);
    }

    @Test
    public void testIncorrectUrl() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(URL_MSG_PREFIX + ValidateArgument.NOT_VALID_URL_MSG);

        isValidUrl(URL_MSG_PREFIX, TEST_INCORRECT_URL);

    }

}
