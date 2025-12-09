package dev.alexissdev.crudapp.security.token;

import dev.alexissdev.crudapp.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String token;
    private Instant expiration;

    @ManyToOne
    private User user;

}
