package com.aradata.plaidapp.service.content;

import com.aradata.plaidapp.exception.FileIsNotImageException;
import com.aradata.plaidapp.exception.ResourceNotFoundException;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFSDBFile;
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
public class ImageService {

	@Autowired
	private GridFsOperations operations;


	public String store(MultipartFile file) throws IOException {
		if(!file.getContentType().contains("image"))
			throw new FileIsNotImageException("File should be image");
		ObjectId store = operations.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
		return store.toHexString();
	}

	public GridFsResource getImageById(String imageId) {
		GridFSFile id = operations.findOne(new Query(Criteria.where("_id").is(imageId)));
		if(id == null) {
			throw new ResourceNotFoundException("image", "id", imageId);
		}
		return operations.getResource(id);

	}
}
