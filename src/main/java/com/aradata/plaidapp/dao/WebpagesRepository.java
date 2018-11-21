package com.aradata.plaidapp.dao;

import com.aradata.plaidapp.model.Webpage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "webpage", path = "webpage")
public interface WebpagesRepository extends MongoRepository<Webpage, String> {
}
