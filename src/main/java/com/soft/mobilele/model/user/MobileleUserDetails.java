package com.soft.mobilele.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MobileleUserDetails implements UserDetails {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return true;
    }

    public String getFullName() {

        StringBuilder sb = new StringBuilder();

        if (firstName != null) {
            sb.append(firstName);
        }

        if (!sb.isEmpty()) {
            sb.append(" ");
        }

        if (lastName != null) {
            sb.append(lastName);
        }

        return sb.isEmpty() ? username : sb.toString();
    }
}
