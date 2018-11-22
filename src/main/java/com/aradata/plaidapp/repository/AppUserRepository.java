package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AppUserRepository extends MongoRepository<AppUser, String> {

	Optional<AppUser> findByUsername(String username);

	Optional<AppUser> findByUsernameOrEmail(String username, String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
