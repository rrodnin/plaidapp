package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.service.content.YoutubeVideo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface YoutubeVideoRepository extends MongoRepository<YoutubeVideo, String> {

	boolean existsByYoutubeId(String youtubeId);
}
