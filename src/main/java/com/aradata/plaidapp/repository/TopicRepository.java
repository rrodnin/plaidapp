package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<Topic, String> {
}
