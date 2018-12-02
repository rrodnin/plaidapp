package com.aradata.plaidapp.service.content;

import com.aradata.plaidapp.exception.BadRequestException;
import com.aradata.plaidapp.exception.ContentAlreadyHasPodcastException;
import com.aradata.plaidapp.exception.ContentIsNotPodcastException;
import com.aradata.plaidapp.exception.ResourceNotFoundException;
import com.aradata.plaidapp.model.comments.Comment;
import com.aradata.plaidapp.model.comments.CommentResponse;
import com.aradata.plaidapp.model.content.*;
import com.aradata.plaidapp.model.content.request.CommentRequest;
import com.aradata.plaidapp.model.content.request.ContentRequest;
import com.aradata.plaidapp.model.content.response.ContentResponse;
import com.aradata.plaidapp.model.payloads.PagedResponse;
import com.aradata.plaidapp.model.likes.Like;
import com.aradata.plaidapp.repository.ContentRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.UsersService;
import com.aradata.plaidapp.service.comment.CommentService;
import com.aradata.plaidapp.service.likes.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class ContentService {

	@Autowired
	private ContentRepository repository;

	@Autowired
	private CommentService commentService;

	@Autowired
	private LikeService likeService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private UsersService usersService;

	public PagedResponse<ContentResponse> fetchAllContent(UserPrincipal currentUser, int page, int size) {
		validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Content> contents = repository.findAll(pageable);

		if(contents.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), contents.getNumber(),
					contents.getSize(), contents.getTotalElements(), contents.getTotalPages());
		}

		List<ContentResponse> responseList = contents.map(content ->
				createContentResponseFromContent(currentUser.getId(),content)).getContent();


		return new PagedResponse<>(responseList, contents.getNumber(), contents.getSize(),
				contents.getTotalElements(), contents.getTotalPages());
	}

	private void validatePageNumberAndSize(int page, int size) {
		if(page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if(size > AppConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
		}
	}

	public Content validateContentId(String contentId) {
		return repository.findById(contentId).orElseThrow(() ->
				new ResourceNotFoundException("Content", "id", contentId));
	}

	public Content createContent(@Valid ContentRequest contentRequest) {
		Content content = new Content();
		content.setText(contentRequest.getText());
		content.setTitle(contentRequest.getTitle());
		content.setDescription(contentRequest.getDescription());
		content.setType(contentRequest.getType());
		content.setUrl(contentRequest.getUrl());
		repository.save(content);
		return content;
	}

	public ContentResponse getContentById(UserPrincipal currentUser, String contentId) {
		Content content = validateContentId(contentId);

		return createContentResponseFromContent(currentUser.getId(), content);
	}

	private ContentResponse createContentResponseFromContent(String id, Content content) {
		ContentResponse fromContent = ContentResponse.createFromContent(content);
		fromContent.setUserLikes(likeService.existsByOwnerIdAndContentId(id, content.getId()));
		return fromContent;
	}

	public PagedResponse<Comment> fetchComments(UserPrincipal currentUser, String contentId, int page, int size) {
		validatePageNumberAndSize(page, size);
		validateContentId(contentId);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

		Page<Comment> comments = commentService.findAllByContentId(pageable, contentId);

		if(comments.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), comments.getNumber(),
					comments.getSize(), comments.getTotalElements(), comments.getTotalPages());
		}

		List<Comment> responseList = comments.getContent();

		return new PagedResponse<>(responseList, comments.getNumber(), comments.getSize(),
				comments.getTotalElements(), comments.getTotalPages());

	}

	public Comment createComment(UserPrincipal currentUser, CommentRequest request, String contentId) {
		validateContentId(contentId);
		return commentService.createComment(currentUser, request, contentId);
	}

	public Comment replyToComment(UserPrincipal currentUser, CommentRequest request, String contentId, String commentId) {
		validateContentId(contentId);
		return commentService.reply(currentUser, request, contentId, commentId);
	}

	private CommentResponse createCommentResponseFromComent(UserPrincipal currentUser, Comment comment) {
		CommentResponse response = CommentResponse.createFromComment(comment);
		response.setOwnerName(usersService.findById(currentUser.getId()).getName());
		return response;
	}

	public Like createLike(String contentId, UserPrincipal currentUser) {
		validateContentId(contentId);
		Like like = likeService.createLike(contentId, currentUser);
		Content content = repository.findById(contentId).get();
		content.setLikes(content.getLikes() + 1);
		repository.save(content);
		return like;
	}

	public void deleteLike(String contentId, UserPrincipal currentUser) {
		validateContentId(contentId);
		likeService.deleteLike(contentId, currentUser);
		Content content = repository.findById(contentId).get();
		content.setLikes(content.getLikes() - 1);
		repository.save(content);
	}

	public String addImage(String contentId, MultipartFile file) throws IOException {
		Content content = validateContentId(contentId);
		String store = imageService.store(file);
		Image image = new Image();

		String path = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/images/{imageId}")
				.buildAndExpand(store).toUriString();
		image.setUrl(path);
		image.setId(store);
		content.addImage(image);
		repository.save(content);

		return store;
	}

	public Content getContentById(String contentId) {
		return validateContentId(contentId);
	}

	public PagedResponse<ContentResponse> findAllById(LinkedList<String> ids, int page, int size, UserPrincipal currentUser) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Content> contents = repository.findAllByIdIsIn(ids, pageable);

		if(contents.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), contents.getNumber(),
					contents.getSize(), contents.getTotalElements(), contents.getTotalPages());
		}

		List<ContentResponse> responseList = contents.map(content ->
				createContentResponseFromContent(currentUser.getId(),content)).getContent();

		return new PagedResponse<>(responseList, contents.getNumber(), contents.getSize(),
				contents.getTotalElements(), contents.getTotalPages());
	}

	public Content saveContent(Content content) {
		return repository.save(content);
	}

	public void addView(String contentId) {
		Content content = validateContentId(contentId);
		content.getExtra().setViews(content.getExtra().getViews() + 1);
		repository.save(content);
	}

	public void listen(String contentId) {
		Content content = validateContentId(contentId);
		content.getExtra().setPodcastListening(content.getExtra().getPodcastListening() + 1);
		repository.save(content);
	}

	public boolean existsByGuid(String guid) {
		return repository.existsByGuid(guid);
	}

	public PagedResponse<ContentResponse> searchByType(UserPrincipal currentUser, int page, int size, Type type) {
		validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Content> contents = repository.findAllByType(type, pageable);

		if(contents.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), contents.getNumber(),
					contents.getSize(), contents.getTotalElements(), contents.getTotalPages());
		}

		List<ContentResponse> responseList = contents.map(content ->
				createContentResponseFromContent(currentUser.getId(),content)).getContent();


		return new PagedResponse<>(responseList, contents.getNumber(), contents.getSize(),
				contents.getTotalElements(), contents.getTotalPages());
	}
}
