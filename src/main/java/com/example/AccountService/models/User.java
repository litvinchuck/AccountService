package com.example.AccountService.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Userdetails")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String lastName;


    @Column(unique = true)
    private String email;

    private String password;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private List<Role> roles = new ArrayList<>();

    public User() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    public void grantAuthority(Role authority) {
        roles.add(authority);
    }

    public List<GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return authorities;
    }

}
