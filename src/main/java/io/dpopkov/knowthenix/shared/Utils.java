package io.dpopkov.knowthenix.shared;

import io.dpopkov.knowthenix.services.dto.BaseDto;

public class Utils {
    public static boolean anyFieldIsMissing(String... fields) {
        for (String field : fields) {
            if (field == null || field.isBlank()) {
                return true;
            }
        }
        return false;
    }

    public static boolean idIsMissing(BaseDto dto) {
        return dto.getId() == null;
    }

    public static boolean anyFieldOrIdIsMissing(BaseDto dto, String... fields) {
        if (dto.getId() == null) {
            return true;
        }
        for (String field : fields) {
            if (field == null || field.isBlank()) {
                return true;
            }
        }
        return false;
    }
}
