package com.aradata.plaidapp.dao;


import com.aradata.plaidapp.model.Podcast;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "podcast", path = "podcast")
public interface PodcastsRepository extends MongoRepository<Podcast, String> {
}
