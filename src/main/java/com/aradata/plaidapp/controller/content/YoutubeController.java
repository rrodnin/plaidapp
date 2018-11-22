package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.model.content.AppConstants;
import com.aradata.plaidapp.model.content.YoutubeContent;
import com.aradata.plaidapp.model.content.request.YoutubeContentRequest;
import com.aradata.plaidapp.model.content.response.PagedResponse;
import com.aradata.plaidapp.model.content.response.YoutubeContentResponse;
import com.aradata.plaidapp.model.payloads.ApiResponse;
import com.aradata.plaidapp.security.CurrentUser;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.content.YoutubeContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/content/youtube")
public class YoutubeController {

	@Autowired
	private YoutubeContentService service;

	@GetMapping
	public PagedResponse<YoutubeContentResponse>
	getYoutubeContent(@CurrentUser UserPrincipal currentUser,
	                  @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
	                  @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return service.getAllYoutubeContent(currentUser, page, size);
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional
	public ResponseEntity<?> createYoutubeContent(@Valid @RequestBody YoutubeContentRequest youtubeContentRequest) {
		YoutubeContent content = service.createYoutubeContent(youtubeContentRequest);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{contentId}")
				.buildAndExpand(content.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "Youtube content Created Successfully"));
	}

	@GetMapping("/{contentId}")
	public YoutubeContentResponse getPollById(@CurrentUser UserPrincipal currentUser,
	                                @PathVariable("contentId") String youtubeContentId) {
		return service.getYoutubeContentById(youtubeContentId, currentUser);
	}

	@PostMapping("/{contentId}/like")
	@Transactional
	public ResponseEntity<?> setLike(@CurrentUser UserPrincipal currentUser,
	                                 @PathVariable("contentId") String youtubeContentId) {

		service.setLike(currentUser, youtubeContentId);
		return new ResponseEntity<>(new ApiResponse(true, "Like was added"),  HttpStatus.OK);
	}

	@DeleteMapping("/{contentId}/like")
	public ResponseEntity<?> deleteLike(@CurrentUser UserPrincipal currentUser,
	                                    @PathVariable("contentId") String youtubeContentId) {
		service.deleteLike(currentUser, youtubeContentId);
		return new ResponseEntity<>(new ApiResponse(true, "Like was deleted"),  HttpStatus.OK);
	}

}
