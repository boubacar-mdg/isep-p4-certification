package com.isep.certification.users.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.isep.certification.users.models.enums.Permission.ADMIN_CREATE;
import static com.isep.certification.users.models.enums.Permission.ADMIN_DELETE;
import static com.isep.certification.users.models.enums.Permission.ADMIN_READ;
import static com.isep.certification.users.models.enums.Permission.ADMIN_UPDATE;
import static com.isep.certification.users.models.enums.Permission.EMPLOYEE_CREATE;
import static com.isep.certification.users.models.enums.Permission.EMPLOYEE_DELETE;
import static com.isep.certification.users.models.enums.Permission.EMPLOYEE_READ;
import static com.isep.certification.users.models.enums.Permission.EMPLOYEE_UPDATE;
import static com.isep.certification.users.models.enums.Permission.MANAGER_CREATE;
import static com.isep.certification.users.models.enums.Permission.MANAGER_DELETE;
import static com.isep.certification.users.models.enums.Permission.MANAGER_READ;
import static com.isep.certification.users.models.enums.Permission.MANAGER_UPDATE;
import static com.isep.certification.users.models.enums.Permission.RENTER_CREATE;
import static com.isep.certification.users.models.enums.Permission.RENTER_DELETE;
import static com.isep.certification.users.models.enums.Permission.RENTER_READ;
import static com.isep.certification.users.models.enums.Permission.RENTER_UPDATE;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

  USER(Collections.emptySet()),
  RENTER(
          Set.of(
                  RENTER_READ,
                  RENTER_UPDATE,
                  RENTER_DELETE,
                  RENTER_CREATE
          )
  ),
  ADMIN(
          Set.of(
                  ADMIN_READ,
                  ADMIN_UPDATE,
                  ADMIN_DELETE,
                  ADMIN_CREATE,
                  MANAGER_READ,
                  MANAGER_UPDATE,
                  MANAGER_DELETE,
                  MANAGER_CREATE
          )
  ),
  MANAGER(
          Set.of(
                  MANAGER_READ,
                  MANAGER_UPDATE,
                  MANAGER_DELETE,
                  MANAGER_CREATE
          )
  ),
  AGENT(
          Set.of(
                  EMPLOYEE_READ,
                  EMPLOYEE_UPDATE,
                  EMPLOYEE_DELETE,
                  EMPLOYEE_CREATE
          )
  )

  ;

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
