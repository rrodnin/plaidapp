package com.aradata.plaidapp.service.content;

import com.aradata.plaidapp.exception.ContentIsNotPodcastException;
import com.aradata.plaidapp.exception.ResourceNotFoundException;
import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.Type;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PodcastService {

	@Autowired
	private ContentService service;

	@Autowired
	private GridFsOperations operations;

	public String store(MultipartFile file, String contentId) throws IOException {
		Content contentById = service.getContentById(contentId);
		if (contentById.getType() != Type.PODCAST)
			throw new ContentIsNotPodcastException();
		DBObject metaData = new BasicDBObject();
		((BasicDBObject) metaData).put("type", "podcast");
		((BasicDBObject) metaData).put("contentId", contentId);
		ObjectId store = operations.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
		return store.toHexString();
	}

	public GridFsResource getPodcastById(String imageId) {
		GridFSFile id = operations.findOne(new Query(Criteria.where("_id").is(imageId)));
		if(id == null) {
			throw new ResourceNotFoundException("podcast", "id", imageId);
		}
		return operations.getResource(id);
	}

	public String getPodcastIdByContentId(String contentId) {
		GridFSFile one = operations.findOne(new Query(Criteria.where("metadata.contentId").is(contentId)));
		if (one == null) {
			return "";
		}
		return one.getObjectId().toHexString();
	}
}
