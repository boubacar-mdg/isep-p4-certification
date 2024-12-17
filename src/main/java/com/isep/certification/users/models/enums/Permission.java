package com.isep.certification.users.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete"),
    RENTER_READ("renter:read"),
    RENTER_UPDATE("renter:update"),
    RENTER_CREATE("renter:create"),
    RENTER_DELETE("renter:delete"),
    EMPLOYEE_READ("employement:read"),
    EMPLOYEE_UPDATE("employement:update"),
    EMPLOYEE_CREATE("employement:create"),
    EMPLOYEE_DELETE("employement:delete"),

    ;

    @Getter
    private final String permission;
}
