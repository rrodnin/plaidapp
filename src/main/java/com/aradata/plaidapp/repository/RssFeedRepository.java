package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.content.PodcastFeed;
import com.aradata.plaidapp.model.content.RssFeed;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RssFeedRepository extends MongoRepository<RssFeed, String> {
		boolean existsByUrl(String url);
}
