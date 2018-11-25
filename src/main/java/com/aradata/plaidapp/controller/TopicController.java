package com.aradata.plaidapp.controller;

import com.aradata.plaidapp.model.content.AppConstants;
import com.aradata.plaidapp.model.content.request.AddContentToTopicRequest;
import com.aradata.plaidapp.model.content.request.TopicRequest;
import com.aradata.plaidapp.model.content.response.ContentResponse;
import com.aradata.plaidapp.model.payloads.ApiResponse;
import com.aradata.plaidapp.model.payloads.PagedResponse;
import com.aradata.plaidapp.security.CurrentUser;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.content.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

	@Autowired
	private TopicService service;

	@PostMapping
	public ResponseEntity<?> createTopic(@Valid @RequestBody TopicRequest request) {
		String topic = service.createTopic(request);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/api/topics/{topicId}")
				.buildAndExpand(topic)
				.toUri();

		return ResponseEntity.created(uri).body(new ApiResponse<>(true, "topic created", 201));
	}

	@PostMapping("/{topicId}")
	public ResponseEntity<?>  addContentToTopic(@PathVariable String topicId,
	                                            @RequestBody @Valid AddContentToTopicRequest request) {
		service.addContentToTopic(topicId, request.getContentId());
		return ResponseEntity.ok().body(new ApiResponse<>(true, "content added", 201));
	}


	@GetMapping("/{topicId}")
	public ResponseEntity<?> getContentByTopicId(@PathVariable String topicId,
												@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
												@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
												@CurrentUser UserPrincipal currentUser) {
		PagedResponse<ContentResponse> contentByTopicId = service.getContentByTopicId(topicId, page, size, currentUser);
		return ResponseEntity.ok().body(contentByTopicId);
	}

	@GetMapping
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok().body(service.fetchAll());
	}
}
