package com.aradata.plaidapp.repository;

import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ContentRepository  extends MongoRepository<Content, String> {

	Page<Content> findAllById(Iterable<String> ids, Pageable pageable);

	boolean existsByGuid(String guid);

	Page<Content> findAllByIdIsIn(Iterable<String> ids, Pageable pageable);

	Page<Content> findAllByType(Type type, Pageable pageable);

	Page<Content> findAllByCategoriesContaining(Set<String> categories, Pageable pageable);
}
