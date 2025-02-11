package xyz.sadiulhakim.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;

@Component
class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user.getId() == null || user.getId() == 0)
            return null;

        return new CustomUserDetails(user.getUsername(), user.getName(), user.getPassword(), user.getRole());
    }
}
