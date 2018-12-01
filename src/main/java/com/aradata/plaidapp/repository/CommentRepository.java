package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.comments.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {

	Page<Comment> findAllByContentIdAndIsReply(Pageable pageable, String contentId, boolean isReply);

	Page<Comment> findAllByContentId(Pageable pageable, String contentId);
}
