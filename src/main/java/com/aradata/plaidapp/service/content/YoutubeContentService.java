package com.aradata.plaidapp.service.content;

import com.aradata.plaidapp.model.content.AppConstants;
import com.aradata.plaidapp.model.content.Type;
import com.aradata.plaidapp.model.content.YoutubeContent;
import com.aradata.plaidapp.model.content.request.YoutubeContentRequest;
import com.aradata.plaidapp.model.content.response.ModelMapper;
import com.aradata.plaidapp.model.content.response.PagedResponse;
import com.aradata.plaidapp.model.content.response.YoutubeContentResponse;
import com.aradata.plaidapp.repository.YoutubeRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.exception.BadRequestException;
import com.aradata.plaidapp.exception.ResourceNotFoundException;
import com.aradata.plaidapp.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class YoutubeContentService {

	@Autowired
	private YoutubeRepository repository;

	@Autowired
	private LikeService service;

	public PagedResponse<YoutubeContentResponse> getAllYoutubeContent(UserPrincipal currentUser, int page, int size) {
		validatePageNumberAndSize(page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<YoutubeContent> contents = repository.findAll(pageable);

		if(contents.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), contents.getNumber(),
					contents.getSize(), contents.getTotalElements(), contents.getTotalPages());
		}

		List<YoutubeContentResponse> responseList = contents.map(
				ModelMapper::mapYoutubeToYoutubeResponse
		).getContent();

		return new PagedResponse<>(responseList, contents.getNumber(), contents.getSize(),
				contents.getTotalElements(), contents.getTotalPages());

	}

	public YoutubeContent createYoutubeContent(YoutubeContentRequest youtubeContentRequest) {
		YoutubeContent content = new YoutubeContent();
		content.setName(youtubeContentRequest.getName());
		content.setDescription(youtubeContentRequest.getDescription());
		content.setUrl(youtubeContentRequest.getUrl());
		repository.save(content);
		return content;
	}

	public YoutubeContentResponse getYoutubeContentById(String youtubeContentId, UserPrincipal currentUser) {
		YoutubeContent content = repository.findById(youtubeContentId)
				.orElseThrow(() -> new ResourceNotFoundException("Youtube", "id", youtubeContentId));
		return ModelMapper.mapYoutubeToYoutubeResponse(content);
	}

	private void validatePageNumberAndSize(int page, int size) {
		if(page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if(size > AppConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
		}
	}


//	public void setLike(UserPrincipal currentUser, String youtubeContentId) {
//		YoutubeContent content = repository.findById(youtubeContentId).orElseThrow(() ->
//				new ResourceNotFoundException("Youtube", "id", youtubeContentId));
//
//		service.setLike(youtubeContentId, currentUser, Type.YOUTUBE);
//		content.setLikes(content.getLikes() + 1);
//		repository.save(content);
//	}
//
//	public void deleteLike(UserPrincipal currentUser, String youtubeContentId) {
//		YoutubeContent content = repository.findById(youtubeContentId).orElseThrow(() ->
//				new ResourceNotFoundException("Youtube", "id", youtubeContentId));
//
//		service.deleteLike(youtubeContentId, currentUser, Type.YOUTUBE);
//		content.setLikes(content.getLikes() - 1);
//		repository.save(content);
//	}
}
