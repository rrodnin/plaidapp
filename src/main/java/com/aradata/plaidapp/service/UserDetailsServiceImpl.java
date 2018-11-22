package com.aradata.plaidapp.service;

import com.aradata.plaidapp.model.AppUser;
import com.aradata.plaidapp.repository.AppUserRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AppUserRepository repository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		AppUser user = repository.findByUsernameOrEmail(s, s)
				.orElseThrow(() ->
						new UsernameNotFoundException("User not found with username or email : " + s)
				);

		return UserPrincipal.create(user);
	}

	@Transactional
	public UserDetails loadUserById(String id) {
		AppUser user = repository.findById(id).orElseThrow(
				() -> new UsernameNotFoundException("User not found with id : " + id)
		);

		return UserPrincipal.create(user);
	}
}
