package com.sohag.authserver.service;

import com.sohag.authserver.model.ApplicationUser;
import com.sohag.authserver.model.ApplicationUserRole;
import com.sohag.authserver.model.AuthenticatedUser;
import com.sohag.authserver.repository.ApplicationUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashSet;

@Component
@Slf4j
public class ApplicationUserDetailsService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserDetailsService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        log.info("Requesting account info for username {}", loginId);
        long t1 = System.currentTimeMillis();
        // check redis for user information existence
        Mono<ApplicationUser> accountData = Mono.just(applicationUserRepository.findByUsername(loginId));
        log.info("data {}", accountData.block());
        log.info("role {}", accountData.block().getRoles());
        ApplicationUser account;
        try {
            account = accountData.block();
        } catch (Exception ie) {
            throw new UsernameNotFoundException("User name not found");
        }

        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        assert account != null;
        for (ApplicationUserRole role : account.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        long t2 = System.currentTimeMillis();
        log.info("Request took {} ms", t2 - t1);
        Assert.notNull(account, "Account not found");
        return new AuthenticatedUser(loginId, account.getPassword(), account.getIsActive(), account.getIsActive(), Boolean.TRUE, account.getIsActive(), authorities);
    }

}
