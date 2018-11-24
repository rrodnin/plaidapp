package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.likes.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository  extends MongoRepository<Like, String> {
	boolean existsByContentIdAndOwnerId(String contentId, String ownerId);

	void deleteByContentIdAndOwnerId(String contentId, String ownerId);
}
