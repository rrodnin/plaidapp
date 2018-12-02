package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.service.content.YoutubePlaylist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface YoutubePlaylistRepository extends MongoRepository<YoutubePlaylist, String> {
	boolean existsByListId(String id);
}
