package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.model.comments.Comment;
import com.aradata.plaidapp.model.comments.CommentResponse;
import com.aradata.plaidapp.model.content.AppConstants;
import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.Type;
import com.aradata.plaidapp.model.content.request.CommentRequest;
import com.aradata.plaidapp.model.content.request.ContentRequest;
import com.aradata.plaidapp.model.content.response.ContentResponse;
import com.aradata.plaidapp.model.payloads.ErrorResponse;
import com.aradata.plaidapp.model.payloads.PagedResponse;
import com.aradata.plaidapp.model.payloads.ApiResponse;
import com.aradata.plaidapp.security.CurrentUser;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.content.ContentService;
import com.aradata.plaidapp.service.content.ImageService;
import com.aradata.plaidapp.service.content.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
public class ContentController {

	@Autowired
	private ContentService service;

	@Autowired
	private ImageService imageService;

	@Autowired
	private PodcastService podcastService;

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
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> createContent(@Valid @RequestBody ContentRequest contentRequest)  {
		Content content = service.createContent(contentRequest);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{contentId}")
				.buildAndExpand(content.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "Content Created Successfully", 201));
	}

	@GetMapping("/{contentId}")
	public ResponseEntity<ContentResponse> getContentById(@CurrentUser UserPrincipal currentUser,
	                                                      @PathVariable String contentId) {
		ContentResponse content = service.getContentById(currentUser, contentId);
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

		if (content.getType() == Type.PODCAST) {
			try {
				String podcastIdByContentId = podcastService.getPodcastIdByContentId(content.getContentId());
				if (!podcastIdByContentId.isEmpty()) {
					content.add(linkTo(methodOn(PodcastsController.class)
							.getPodcast(podcastIdByContentId)).withRel("podcast"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


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
		CommentResponse comment = service.createComment(currentUser, request, contentId);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/comments/{commentId}")
				.buildAndExpand(comment.getCommentId()).toUri();

		ApiResponse<CommentResponse> apiResponse = new ApiResponse<>(true, "Comment created successfully", 201);
		apiResponse.setData(comment);

		return ResponseEntity.created(location)
				.body(apiResponse);
	}

	@PostMapping("/{contentId}/comments/{commentId}")
	public ResponseEntity<?> replyToComment(@PathVariable String contentId, @PathVariable String commentId,
	                                        @CurrentUser UserPrincipal currentUser,
	                                        @Valid @RequestBody CommentRequest request) {
		CommentResponse comment = service.replyToComment(currentUser, request, contentId, commentId);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/comments/{commentId}")
				.buildAndExpand(comment.getCommentId()).toUri();

		ApiResponse<CommentResponse> apiResponse = new ApiResponse<>(true, "Comment created successfully", 201);
		apiResponse.setData(comment);
		return ResponseEntity.created(location)
				.body(apiResponse);
	}

	/** ---LIKES--- **/
	@PostMapping("/{contentId}/likes")
	@Transactional
	public ResponseEntity<?> createLike(@PathVariable String contentId,
	                                    @CurrentUser UserPrincipal currentUser) {
			service.createLike(contentId, currentUser);
			return ResponseEntity.ok().body(new ApiResponse(true, "Like was created", 201));
	}

	@DeleteMapping("/{contentId}/likes")
	@Transactional
	public ResponseEntity<?> deleteLike(@PathVariable String contentId,
	                                    @CurrentUser UserPrincipal currentUser) {
		service.deleteLike(contentId, currentUser);
		return ResponseEntity.ok().body(new ApiResponse(true, "Like was deleted", 200));
	}

	/** ---IMAGES--- **/

	@PostMapping("/{contentId}/images")
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> uploadImage(@PathVariable String contentId,
							@RequestParam("file") MultipartFile file) throws IOException {
		String uriString = service.addImage(contentId, file);
		URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/images/{imageId}").buildAndExpand(
				uriString
		).toUri();
		return ResponseEntity.created(uri).body(new ApiResponse(
				true,
				"Image uploaded successfully",
				201
		));
	}


	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity validationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		final List<FieldError> fieldErrors = result.getFieldErrors();

		return ResponseEntity.badRequest().body(new ErrorResponse(400, fieldErrors.get(0).getField() + " " +
				fieldErrors.get(0).getDefaultMessage()));
	}

}
