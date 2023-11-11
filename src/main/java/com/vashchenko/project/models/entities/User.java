package com.vashchenko.project.models.entities;

import com.vashchenko.project.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"bookingList","name","surname","patronymic","phoneNumber","mail"})
@ToString(exclude = "bookingList")
@Table(name = "user_table")

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    @Column(nullable = false)
    private boolean active=true;
    @Column(nullable = false)
    private String login;
    @Column(nullable = false)
    private String password;
    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;
    private String mail;
    private String name;
    private String surname;
    private String patronymic;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Booking> bookingList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public void addRole(Role role){
        this.roles.add(role);
    }
}
