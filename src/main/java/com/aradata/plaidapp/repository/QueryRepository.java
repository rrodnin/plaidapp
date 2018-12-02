package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.service.content.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QueryRepository  extends MongoRepository<Query, String> {
}
