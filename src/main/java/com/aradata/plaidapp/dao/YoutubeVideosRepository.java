package com.aradata.plaidapp.dao;

import com.aradata.plaidapp.model.YoutubeVideo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "youtubevideo", path = "youtubevideo")
public interface YoutubeVideosRepository extends MongoRepository<YoutubeVideo, String> {

	List<YoutubeVideo> findByName(@Param("name") String name);
}
