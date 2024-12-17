package com.isep.certification.users.models.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isep.certification.countries.models.entities.Country;
import com.isep.certification.tokens.models.entities.Token;
import com.isep.certification.users.models.enums.AccountState;
import com.isep.certification.users.models.enums.Role;
import com.isep.certification.users.models.enums.Subscription;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_users")
  @SequenceGenerator(name = "seq_users", sequenceName = "seq_users", allocationSize = 20, initialValue = 10)
  @JsonIgnore
  private Long id;
  private String fullName;
  private String phoneNumber;
  private String email;
  private Long credit;
  @JsonIgnore
  private String password;
  @JsonIgnore
  private String address;
  @Column(columnDefinition = "TEXT")
  private String avatar;
  @JsonIgnore
  private String firebaseToken;
  @JsonIgnore
  private LocalDateTime lastSyncroTime;
  private LocalDateTime createAt;
  
  @JsonIgnore
  @Enumerated(EnumType.STRING)
  private AccountState accountState;

  @ManyToOne
  private UserSubscription subscription;

  @JsonIgnore
  @Enumerated(EnumType.STRING)
  private Role role;

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @ManyToOne
  private Country country;

/*   @OneToMany(mappedBy = "user")
  private List<Transaction> transactions; */

  @JsonIgnore
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return phoneNumber;
  }
 
  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return true;
  }
}
