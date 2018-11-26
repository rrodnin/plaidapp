package com.aradata.plaidapp.controller.users;

import com.aradata.plaidapp.controller.content.ContentController;
import com.aradata.plaidapp.model.content.AppConstants;
import com.aradata.plaidapp.model.content.response.ContentResponse;
import com.aradata.plaidapp.model.payloads.PagedResponse;
import com.aradata.plaidapp.security.CurrentUser;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
public class UsersController {

	@Autowired
	private UsersService usersService;

	@GetMapping("/news")
	public ResponseEntity<?> getNews(@CurrentUser UserPrincipal currentUser,
	                                 @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
	                                 @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		PagedResponse<ContentResponse> response = usersService.getUserNews(currentUser, page, size);
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
}
