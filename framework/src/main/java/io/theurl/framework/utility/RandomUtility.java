package io.theurl.framework.utility;

@SuppressWarnings("unused")
public class RandomUtility {
    private static final java.util.Random random = new java.util.Random();

    /**
     * Generates a random alphanumeric string of the specified length.
     *
     * @param length The length of the random string to generate.
     * @return A random alphanumeric string of the specified length.
     */
    public static String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
