package io.theurl.framework.utility;

@SuppressWarnings("unused")
public class RandomUtility {
    private static final java.util.Random random = new java.util.Random();

    private static final String ALPHA_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC_CHARS = "0123456789";
    private static final String MIXED_CHARS = ALPHA_CHARS + NUMERIC_CHARS;

    /**
     * Generates a random string of the specified length using the provided characters.
     *
     * @param length     The length of the random string to generate.
     * @param characters The characters to use for generating the random string.
     * @return A random string of the specified length using the provided characters.
     * @throws IllegalArgumentException if the length is less than or equal to 0 or if the characters string is shorter than the specified length.
     */
    public static String randomString(int length, String characters) {

        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        if (characters.length() < length) {
            throw new IllegalArgumentException("The length of the characters must be at least " + length);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a random alphanumeric string of the specified length.
     *
     * @param length The length of the random string to generate.
     * @return A random alphanumeric string of the specified length.
     */
    public static String randomString(int length) {
        return randomString(length, MIXED_CHARS);
    }

    /**
     * Generates a random string of the specified length and mode.
     *
     * @param length The length of the random string to generate.
     * @param mode   The mode of the random string (ALPHA, NUMERIC, or MIXED).
     * @return A random string of the specified length and mode.
     */
    public static String randomString(int length, RandomUtility.Mode mode) {

        String characters = switch (mode) {
            case ALPHA -> ALPHA_CHARS;
            case NUMERIC -> NUMERIC_CHARS;
            case MIXED -> MIXED_CHARS;
        };

        return randomString(length, characters);
    }

    /**
     * Enumeration representing the mode of random string generation.
     */
    public enum Mode {
        /**
         * Generates a random string consisting of alphabetic characters (both uppercase and lowercase).
         */
        ALPHA,

        /**
         * Generates a random string consisting of numeric characters (digits 0-9).
         */
        NUMERIC,

        /**
         * Generates a random string consisting of both alphabetic and numeric characters.
         */
        MIXED
    }
}
