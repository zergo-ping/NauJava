package ru.Nikita.NauJava.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Реализация UserDetailsService для загрузки информации о пользователе из базы данных
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        }

        return new User(
            user.getEmail(),
            user.getPasswordHash(),
            authorities
        );
    }
}
