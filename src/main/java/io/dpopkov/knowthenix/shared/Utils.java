package io.dpopkov.knowthenix.shared;

public class Utils {
    public static boolean anyFieldIsMissing(String... fields) {
        for (String field : fields) {
            if (field == null || field.isBlank()) {
                return true;
            }
        }
        return false;
    }
}
