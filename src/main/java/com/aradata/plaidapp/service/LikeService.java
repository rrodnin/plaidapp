package com.aradata.plaidapp.service;

import com.aradata.plaidapp.exception.BadRequestException;
import com.aradata.plaidapp.model.content.Like;
import com.aradata.plaidapp.model.content.Type;
import com.aradata.plaidapp.repository.LikeRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

	@Autowired
	private LikeRepository likeRepository;

	public void setLike(String contentId, UserPrincipal principal, Type contentType) {
		String userId = principal.getId();
		if (likeRepository.existsByContentIdAndAndOwnerIdAndContentType(contentId,
				principal.getId(), contentType)) {
			throw new BadRequestException("Like is already set");
		}
		Like like = new Like();
		like.setContentId(contentId);
		like.setContentType(contentType);
		like.setOwnerId(principal.getId());
		likeRepository.save(like);
	}

	public void deleteLike(String youtubeContentId, UserPrincipal currentUser, Type type) {
		String userId = currentUser.getId();
		if (!likeRepository.existsByContentIdAndAndOwnerIdAndContentType(youtubeContentId,
				userId, type)) {
			throw new BadRequestException("Like is not set");
		}
		Like like = likeRepository.findByContentIdAndOwnerIdAndContentType(youtubeContentId, userId, type).get();
		likeRepository.delete(like);
	}
}
