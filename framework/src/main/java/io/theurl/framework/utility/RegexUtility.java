package io.theurl.framework.utility;

public class RegexUtility {
    public static boolean isEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean isPhone(String phone) {
        return phone != null && phone.matches("^\\+?[0-9]{7,15}$");
    }
}
