package com.aradata.plaidapp.service;

import com.aradata.plaidapp.exception.ResourceNotFoundException;
import com.aradata.plaidapp.model.user.AppUser;
import com.aradata.plaidapp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

	@Autowired
	private AppUserRepository repository;

	public AppUser findById(String id) {
		AppUser user = repository.findById(id).orElseThrow(() ->new ResourceNotFoundException("user", "id", id));
		return user;
	}
}
