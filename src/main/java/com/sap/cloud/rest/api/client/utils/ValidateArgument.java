package com.sap.cloud.rest.api.client.utils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility class for argument validations.
 *
 */
public class ValidateArgument {

    public static final String CANNOT_BE_NULL_MSG = " cannot be null.";
    public static final String CANNOT_BE_BLANK_MSG = " cannot be blank.";
    public static final String NOT_VALID_URL_MSG = " is not a vlaid URL.";

    /**
     * Validates that the strings are not null or empty.
     * 
     * If a string is not valid - throws IllegalArgumentException.
     * 
     * @param messagePrefix
     *            a display message for the tested strings.
     * @param strings
     *            the strings that will be validated.
     */
    public static void isNotEmptyOrNull(String messagePrefix, String... strings) {
        for (String string : strings) {
            if (ValidateArgument.isEmpty(string)) {
                throw new IllegalArgumentException(messagePrefix + CANNOT_BE_BLANK_MSG);
            }
        }
    }

    private static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * Validates that the char array is not null or empty.
     * 
     * If the char array is not valid - throws IllegalArgumentException.
     * 
     * @param messagePrefix
     *            a display message for the tested array.
     * @param charArray
     *            the char array that will be validated.
     */
    public static void isNotEmptyOrNull(String messagePrefix, char[] charArray) {
        if (charArray == null || charArray.length < 1) {
            throw new IllegalArgumentException(messagePrefix + CANNOT_BE_BLANK_MSG);
        }
    }

    /**
     * Validates that the objects are not null.
     * 
     * If an object is null - throws IllegalArgumentException.
     * 
     * @param messagePrefix
     *            a display message for the tested objects.
     * @param objects
     *            the objects that will be validated.
     */
    public static void isNotNull(String messagePrefix, Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                throw new IllegalArgumentException(messagePrefix + CANNOT_BE_NULL_MSG);
            }
        }
    }

    /**
     * Validates that the object is a correct URL
     * 
     * If an object is incorrect URL - throws IllegalArgumentException.
     * 
     * @param messagePrefix
     *            - a display message for the URL
     * @param url
     *            - the URL that will be validated.
     */
    public static void isValidUrl(String messagePrefix, String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException(messagePrefix + NOT_VALID_URL_MSG, e);
        }
    }

}
