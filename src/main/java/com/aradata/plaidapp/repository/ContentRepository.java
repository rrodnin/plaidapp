package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.content.Content;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository  extends MongoRepository<Content, String> {

}
