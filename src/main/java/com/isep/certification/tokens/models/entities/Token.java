package com.isep.certification.tokens.models.entities;

import com.isep.certification.tokens.models.enums.TokenType;
import com.isep.certification.users.models.entities.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tokens")
  @SequenceGenerator(name = "seq_tokens", sequenceName = "seq_tokens", allocationSize = 20, initialValue = 10)
  public Integer id;

  @Column(unique = true)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;

  public boolean revoked;

  public boolean expired;

  @ManyToOne
  @JoinColumn(name = "user_id")
  public User user;
}
