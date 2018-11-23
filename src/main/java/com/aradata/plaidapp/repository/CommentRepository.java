package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.content.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository  extends MongoRepository<Comment, String> {
}
