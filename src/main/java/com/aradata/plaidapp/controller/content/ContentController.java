package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.model.content.AppConstants;
import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.request.ContentRequest;
import com.aradata.plaidapp.model.content.response.PagedResponse;
import com.aradata.plaidapp.model.payloads.ApiResponse;
import com.aradata.plaidapp.security.CurrentUser;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.content.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/content")
public class ContentController {

	@Autowired
	private ContentService service;

	@GetMapping
	public PagedResponse<Content> getContents(@CurrentUser UserPrincipal currentUser,
	                                          @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
	                                          @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return service.fetchAllContent(currentUser, page, size);
	}

	@PostMapping
	public ResponseEntity<?> createContent(@Valid @RequestBody ContentRequest contentRequest)  {
		Content content = service.createContent(contentRequest);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{contentId}")
				.buildAndExpand(content.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "Content Created Successfully"));
	}

	@GetMapping("/{contentId}")
	public Content getContentById(@PathVariable String contentId) {
		return service.getContentById(contentId);
	}
}
