package io.theurl.framework.utility;

public class RegexUtility {
    public static final String PHONE_REGEX = "^\\+?[0-9]{7,15}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static boolean isEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    public static boolean isPhone(String phone) {
        return phone != null && phone.matches(PHONE_REGEX);
    }
}
