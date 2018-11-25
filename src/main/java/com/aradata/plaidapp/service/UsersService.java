package com.aradata.plaidapp.service;

import com.aradata.plaidapp.exception.ResourceNotFoundException;
import com.aradata.plaidapp.model.content.response.ContentResponse;
import com.aradata.plaidapp.model.payloads.PagedResponse;
import com.aradata.plaidapp.model.user.AppUser;
import com.aradata.plaidapp.repository.AppUserRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.content.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

	@Autowired
	private AppUserRepository repository;

	@Autowired
	private ContentService service;

	public AppUser findById(String id) {
		AppUser user = repository.findById(id).orElseThrow(() ->new ResourceNotFoundException("user", "id", id));
		return user;
	}


	public PagedResponse<ContentResponse> getUserNews(UserPrincipal userPrincipal, int page, int size) {
		return service.fetchAllContent(userPrincipal, page, size);
	}
}
