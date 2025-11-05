package com.example.LAAR.AuthService.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User entity representing an application user account.
 * 
 * Annotations:
 *  - {@link Entity}: Marks this class as a JPA entity managed by the persistence context.
 *  - {@link Table}(name = "users"): Maps the entity to the "users" table (avoid reserved word "user").
 *  - {@link Data}: Lombok generates getters, setters, toString, equals, and hashCode.
 *  - {@link NoArgsConstructor}: Generates a no-argument constructor (required by JPA).
 *  - {@link AllArgsConstructor}: Generates a constructor with all fields.
 *  - {@link Builder}: Provides the builder pattern for cleaner object creation.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // Primary key (auto-increment)

  @Column(nullable = false)
  private String name; // Display name / username

  @Column(unique = true, nullable = false)
  private String email; // Unique email identifier

  @Column(nullable = false)
  private String password; // Stored as a hashed value (implement hashing in service layer)
}
