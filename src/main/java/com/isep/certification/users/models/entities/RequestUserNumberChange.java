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
@Table(name = "users_number_change_requests")
public class RequestUserNumberChange {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_users_number_change_requests")
  @SequenceGenerator(name = "seq_users_number_change_requests", sequenceName = "seq_users_number_change_requests", allocationSize = 20, initialValue = 10)
  private Long id;
  private Long userId;
  private String fromNumber;
  private String toNumber;
  private LocalDateTime changDateTime;
  private Boolean isDone;
  
}
