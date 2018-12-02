package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.content.PodcastFeed;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PodcastFeedRepository extends MongoRepository<PodcastFeed, String> {

	boolean existsByUrl(String url);
}
