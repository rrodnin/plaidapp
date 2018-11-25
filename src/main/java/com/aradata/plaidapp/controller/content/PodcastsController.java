package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.model.payloads.ApiResponse;
import com.aradata.plaidapp.service.content.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/podcasts")
public class PodcastsController {

	@Autowired
	private PodcastService service;

	@GetMapping("/{podcastId}")
	@ResponseBody
	public ResponseEntity<InputStreamResource> getImage(@PathVariable String podcastId) throws IOException {
		GridFsResource podcast = service.getPodcastById(podcastId);
		return ResponseEntity.ok()
				.contentLength(podcast.contentLength())
				.contentType(MediaType.parseMediaType(podcast.getContentType()))
				.body(new InputStreamResource(podcast.getInputStream()));
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
		String uriString = service.store(file);
		URI uri = URI.create(uriString);
		return ResponseEntity.created(uri).body(new ApiResponse(
				true,
				"Podcast uploaded successfully",
				201
		));
	}
}
