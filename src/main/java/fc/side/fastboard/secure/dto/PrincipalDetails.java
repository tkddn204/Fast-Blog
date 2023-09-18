package fc.side.fastboard.secure.dto;

import fc.side.fastboard.user.entity.User;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record PrincipalDetails(
        String email,
        String password,
        String username
) implements UserDetails {

    public static PrincipalDetails of(User user) {
        return new PrincipalDetails(user.getEmail(),
                user.getPassword(),
                user.getUsername()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: 역할 관련 협의 필요
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
}
