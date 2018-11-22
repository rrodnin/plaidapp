package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.user.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String> {

	Optional<AppUser> findByUsername(String username);

	Optional<AppUser> findByUsernameOrEmail(String username, String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
