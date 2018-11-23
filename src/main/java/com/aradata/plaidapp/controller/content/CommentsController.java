package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.model.content.Comment;
import com.aradata.plaidapp.model.content.request.CommentRequest;
import com.aradata.plaidapp.model.content.response.CommentResponse;
import com.aradata.plaidapp.model.content.response.PagedResponse;
import com.aradata.plaidapp.model.payloads.ApiResponse;
import com.aradata.plaidapp.security.CurrentUser;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.content.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {


	@Autowired
	private CommentService service;

	@GetMapping("/{commentId}")
	public CommentResponse getCommentById(@PathVariable String commentId) {
		return service.getCommentById(commentId);
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> createComment(@Valid @RequestBody CommentRequest request,
	                                       @CurrentUser UserPrincipal currentUser) {
		Comment comment = service.createComment(request, currentUser);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/comments/{commentId}")
				.buildAndExpand(comment.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "Comment created successfully"));
	}

}
