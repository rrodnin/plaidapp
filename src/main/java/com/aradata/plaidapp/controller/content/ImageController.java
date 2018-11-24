package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.service.content.ImageService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

	@Autowired
	private ImageService imageService;

	@GetMapping("/{imageId}")
	@ResponseBody
	public ResponseEntity<InputStreamResource> getImage(@PathVariable String imageId) throws IOException {
		GridFsResource imageById = imageService.getImageById(imageId);
		return ResponseEntity.ok()
				.contentLength(imageById.contentLength())
				.contentType(MediaType.parseMediaType(imageById.getContentType()))
				.body(new InputStreamResource(imageById.getInputStream()));
	}
}
