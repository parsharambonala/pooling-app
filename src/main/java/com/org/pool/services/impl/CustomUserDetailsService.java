package com.org.pool.services.impl;

import com.org.pool.domain.entities.User;
import com.org.pool.repositories.UserRepository;
import com.org.pool.services.CustomUserDetails;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmployeeMailId(username)
                .orElseThrow(()->
                        new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
