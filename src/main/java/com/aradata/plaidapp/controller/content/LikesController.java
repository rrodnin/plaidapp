package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.model.content.Type;
import com.aradata.plaidapp.model.content.request.LikeRequest;
import com.aradata.plaidapp.model.content.response.PagedResponse;
import com.aradata.plaidapp.model.payloads.ApiResponse;
import com.aradata.plaidapp.security.CurrentUser;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/likes")
public class LikesController {

	@Autowired
	private LikeService service;


	@Transactional
	@PostMapping
	public ResponseEntity<?> setLike(@CurrentUser UserPrincipal currentUser,
	                                 @Valid @RequestBody LikeRequest request) {

		service.setLike(currentUser, request);
		return new ResponseEntity<>(new ApiResponse(true, "Like was added"), HttpStatus.OK);
	}


	@Transactional
	@DeleteMapping("/{contentId}")
	public ResponseEntity<?> deleteLike(@CurrentUser UserPrincipal currentUser,
	                                    @PathVariable("contentId") String contentId,
	                                    @Valid @RequestBody LikeRequest request) {
		service.deleteLike(currentUser, request);
		return new ResponseEntity<>(new ApiResponse(true, "Like was deleted"), HttpStatus.OK);
	}


}
