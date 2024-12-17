package com.isep.certification.users.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_subscriptions")
public class UserSubscription {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_users_subscriptions")
  @SequenceGenerator(name = "seq_users_subscriptions", sequenceName = "seq_users_subscriptions", allocationSize = 20, initialValue = 10)
  private Long id;
  private String name;
  private Long price;
  private Boolean active;
  
}
