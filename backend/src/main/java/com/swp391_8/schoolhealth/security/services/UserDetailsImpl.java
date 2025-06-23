package com.swp391_8.schoolhealth.security.services;

import com.swp391_8.schoolhealth.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private boolean isActive;

    public UserDetailsImpl(Integer id, String username, String email, String fullName, String phoneNumber, String password,
                           Collection<? extends GrantedAuthority> authorities, boolean isActive) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }    public static UserDetailsImpl build(User user) {
        // Create authorities based on the user\'s role
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Add authority from the role field (ManyToOne relationship)
        if (user.getRole() != null && user.getRole().getRoleName() != null) {
            String roleName = user.getRole().getRoleName().toUpperCase();
            if (roleName.startsWith("ROLE_")) {
                authorities.add(new SimpleGrantedAuthority(roleName));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
            }
        }

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getPassword(),
                authorities,
                user.getIsActive());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean getIsActive() {
        return isActive;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
