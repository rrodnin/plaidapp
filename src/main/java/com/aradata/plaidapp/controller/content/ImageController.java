package com.aradata.plaidapp.controller.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

	@Autowired
	private GridFsOperations operations;

	@PostMapping
	@Transactional
	public void uploadImage(@RequestParam("file") MultipartFile file) {
		try {
			operations.store(file.getInputStream(), file.getOriginalFilename());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
