package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.content.YoutubeContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeRepository  extends MongoRepository<YoutubeContent, String> {
}
