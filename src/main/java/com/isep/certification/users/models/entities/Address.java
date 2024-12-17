package com.isep.certification.users.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_addresses")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_users_addresses")
  @SequenceGenerator(name = "seq_users_addresses", sequenceName = "seq_users_addresses", allocationSize = 20, initialValue = 10)
  private Long id;
  private Long userId;
  private String address;
  private LocalDateTime insertDate;
  private Boolean isDefault;
  
}
