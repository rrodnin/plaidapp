package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.model.comments.Comment;
import com.aradata.plaidapp.model.comments.CommentResponse;
import com.aradata.plaidapp.model.content.AppConstants;
import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.request.CommentRequest;
import com.aradata.plaidapp.model.content.request.ContentRequest;
import com.aradata.plaidapp.model.content.response.ContentResponse;
import com.aradata.plaidapp.model.payloads.PagedResponse;
import com.aradata.plaidapp.model.payloads.ApiResponse;
import com.aradata.plaidapp.security.CurrentUser;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.content.ContentService;
import com.aradata.plaidapp.service.content.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/contents")
public class ContentController {

	@Autowired
	private ContentService service;

	@Autowired
	private ImageService imageService;

	/** ---GENERAL--- **/

	@GetMapping
	public ResponseEntity<?>
	getContents(@CurrentUser UserPrincipal currentUser,
	            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
	            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		PagedResponse<ContentResponse> response = service.fetchAllContent(currentUser, page, size);
		response.getContent().stream()
				.forEach(contentResponse -> {
					contentResponse.add(
							linkTo(methodOn(ContentController.class).getContentById(
									currentUser,
									contentResponse.getContentId()
							)).withSelfRel());
					contentResponse.add(
							linkTo(methodOn(ContentController.class).getComments(
									currentUser,contentResponse.getContentId(),0,30
							)).withRel("comments"));
					contentResponse.add(
							linkTo(methodOn(ContentController.class).createLike(
									contentResponse.getContentId(), currentUser
							)).withRel("likes"));
				});

		Link link = linkTo(methodOn(ContentController.class).getContents(currentUser, page, size)).withSelfRel();
		Link linkToNext = linkTo(methodOn(ContentController.class).getContents(currentUser, page, size)).withRel("next");
		Link linkToPrev = linkTo(methodOn(ContentController.class).getContents(currentUser, page, size)).withRel("prev");
		if (page < response.getTotalPages() - 1) {
			linkToNext = linkTo(methodOn(ContentController.class).getContents(currentUser, page+1, size)).withRel("next");
		}
		if (page > 0) {
			linkToPrev = linkTo(methodOn(ContentController.class).getContents(currentUser, page-1, size)).withRel("prev");
		}
		Resource<PagedResponse<ContentResponse>> responseResource = new Resource<>(response, link, linkToNext, linkToPrev);
		return ResponseEntity.ok().body(responseResource);

	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> createContent(@Valid @RequestBody ContentRequest contentRequest)  {
		Content content = service.createContent(contentRequest);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{contentId}")
				.buildAndExpand(content.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "Content Created Successfully"));
	}

	@GetMapping("/{contentId}")
	public ResponseEntity<ContentResponse> getContentById(@CurrentUser UserPrincipal currentUser,
	                                                      @PathVariable String contentId) {
		ContentResponse content = service.getContentById(contentId);
		content.add(
				linkTo(methodOn(ContentController.class).getContentById(
						currentUser,content.getContentId()
				)).withSelfRel());
		content.add(
				linkTo(methodOn(ContentController.class).getComments(
						currentUser,content.getContentId(),0,30
				)).withRel("comments"));
		content.add(
				linkTo(methodOn(ContentController.class).createLike(
						content.getContentId(), currentUser
				)).withRel("likes"));


		return ResponseEntity.ok().body(content);
	}

	/** ---COMMENTS--- **/

	@GetMapping("/{contentId}/comments")
	public ResponseEntity<?> getComments(@CurrentUser UserPrincipal currentUser,
	                                          @PathVariable String contentId,
	                                          @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
	                                          @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		PagedResponse<CommentResponse> pagedResponse = service.fetchComments(currentUser,contentId,
																			page, size);
		Link link = linkTo(methodOn(ContentController.class).getComments(currentUser, contentId, page, size)).withSelfRel();
		Link linkToNext = linkTo(methodOn(ContentController.class).getComments(currentUser, contentId, page, size)).withRel("next");
		Link linkToPrev = linkTo(methodOn(ContentController.class).getComments(currentUser, contentId, page, size)).withRel("prev");
		if (page < pagedResponse.getTotalPages() - 1) {
			linkToNext = linkTo(methodOn(ContentController.class).getContents(currentUser, page+1, size)).withRel("next");
		}
		if (page > 0) {
			linkToPrev = linkTo(methodOn(ContentController.class).getContents(currentUser, page-1, size)).withRel("prev");
		}
		Resource<PagedResponse<CommentResponse>> responseResource = new Resource<>(pagedResponse, link, linkToNext, linkToPrev);
		return ResponseEntity.ok().body(responseResource);
	}

	@PostMapping("/{contentId}/comments")
	@Transactional
	public ResponseEntity<?> createComment(@PathVariable String contentId,
											@CurrentUser UserPrincipal currentUser,
	                                       @Valid @RequestBody CommentRequest request) {
		Comment comment = service.createComment(currentUser, request, contentId);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/comments/{commentId}")
				.buildAndExpand(comment.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "Comment created successfully"));
	}

	/** ---LIKES--- **/
	@PostMapping("/{contentId}/likes")
	@Transactional
	public ResponseEntity<?> createLike(@PathVariable String contentId,
	                                    @CurrentUser UserPrincipal currentUser) {
			service.createLike(contentId, currentUser);
			return ResponseEntity.ok().body(new ApiResponse(true, "Like was created"));
	}

	@DeleteMapping("/{contentId}/likes")
	@Transactional
	public ResponseEntity<?> deleteLike(@PathVariable String contentId,
	                                    @CurrentUser UserPrincipal currentUser) {
		service.deleteLike(contentId, currentUser);
		return ResponseEntity.ok().body(new ApiResponse(true, "Like was deleted"));
	}

	/** ---IMAGES--- **/

	@PostMapping("/{contentId}/images")
	@Transactional
	public ResponseEntity<?> uploadImage(@PathVariable String contentId,
							@RequestParam("file") MultipartFile file) throws IOException {
		String uriString = service.addImage(contentId, file);
		URI uri = URI.create(uriString);
		return ResponseEntity.created(uri).body(new ApiResponse(
				true,
				"Image uploaded successfully"
		));
	}


}
