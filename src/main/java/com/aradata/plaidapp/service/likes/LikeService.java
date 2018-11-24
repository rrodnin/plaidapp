package com.aradata.plaidapp.service.likes;

import com.aradata.plaidapp.exception.LikeAlreadyExistsException;
import com.aradata.plaidapp.exception.LikeIsNotExistsException;
import com.aradata.plaidapp.model.likes.Like;
import com.aradata.plaidapp.repository.LikeRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

	@Autowired
	private LikeRepository likeRepository;

	public Like createLike(String contentId, UserPrincipal currentUser) {
		if (likeRepository.existsByContentIdAndOwnerId(contentId, currentUser.getId()))
			throw new LikeAlreadyExistsException(contentId, currentUser.getId());
		Like like = new Like();
		like.setContentId(contentId);
		like.setOwnerId(currentUser.getId());
		likeRepository.save(like);
		return like;
	}

	public void deleteLike(String contentId, UserPrincipal currentUser) {
		if (!likeRepository.existsByContentIdAndOwnerId(contentId, currentUser.getId()))
			throw new LikeIsNotExistsException(contentId, currentUser.getId());

		likeRepository.deleteByContentIdAndOwnerId(contentId, currentUser.getId());
	}
}
