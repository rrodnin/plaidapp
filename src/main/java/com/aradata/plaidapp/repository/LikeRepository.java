package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.content.Like;
import com.aradata.plaidapp.model.content.Type;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends MongoRepository<Like, String> {

	Boolean existsByContentIdAndAndOwnerIdAndContentType(String contentId, String ownerId, Type contentType);
	Optional<Like> findByContentIdAndOwnerIdAndContentType(String contentId, String ownerId, Type contentType);
}
