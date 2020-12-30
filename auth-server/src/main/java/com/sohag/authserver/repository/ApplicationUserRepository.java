package com.sohag.authserver.repository;

import com.sohag.authserver.model.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Integer> {
    ApplicationUser findByUsername(String username);
}
