package com.aradata.plaidapp.service;

import com.aradata.plaidapp.exception.BadRequestException;
import com.aradata.plaidapp.model.content.Like;
import com.aradata.plaidapp.model.content.Type;
import com.aradata.plaidapp.model.content.request.LikeRequest;
import com.aradata.plaidapp.repository.LikeRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

	@Autowired
	private LikeRepository likeRepository;

	public void setLike(UserPrincipal principal, LikeRequest request) {
		String userId = principal.getId();
		if (likeRepository.existsByContentIdAndAndOwnerIdAndContentType(request.getContentid(),
				principal.getId(), request.getType())) {
			throw new BadRequestException("Like is already set");
		}
		Like like = new Like();
		like.setContentId(request.getContentid());
		like.setContentType(request.getType());
		like.setOwnerId(principal.getId());
		likeRepository.save(like);
	}

	public void deleteLike(UserPrincipal currentUser, LikeRequest request) {
		String userId = currentUser.getId();
		if (!likeRepository.existsByContentIdAndAndOwnerIdAndContentType(request.getContentid(),
				userId, request.getType())) {
			throw new BadRequestException("Like is not set");
		}
		Like like = likeRepository.findByContentIdAndOwnerIdAndContentType(request.getContentid(), userId, request.getType()).get();
		likeRepository.delete(like);
	}
}
