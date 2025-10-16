package com.rustdesk.api.constant;

import lombok.Getter;

/**
 * Status code enumeration
 */
@Getter
public enum StatusCode {

    /**
     * Enabled status
     */
    ENABLE(1, "Enabled"),

    /**
     * Disabled status
     */
    DISABLED(2, "Disabled");

    private final Integer code;
    private final String description;

    StatusCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Get StatusCode from code value
     *
     * @param code the code value
     * @return StatusCode or null if not found
     */
    public static StatusCode fromCode(Integer code) {
        for (StatusCode status : StatusCode.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * Check if the code is valid
     *
     * @param code the code to check
     * @return true if valid, false otherwise
     */
    public static boolean isValid(Integer code) {
        return fromCode(code) != null;
    }
}
