package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.model.content.request.PodcastUrl;
import com.aradata.plaidapp.model.payloads.ApiResponse;
import com.aradata.plaidapp.service.content.PodcastService;
import com.icosillion.podengine.exceptions.DateFormatException;
import com.icosillion.podengine.exceptions.InvalidFeedException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/podcasts")
public class PodcastsController {

	@Autowired
	private PodcastService service;

//	@GetMapping("/{podcastId}")
//	@ResponseBody
//	public ResponseEntity<InputStreamResource> getPodcast(@PathVariable String podcastId) throws IOException {
//		GridFsResource podcast = service.getPodcastById(podcastId);
//		return ResponseEntity.ok()
//				.contentLength(podcast.contentLength())
//				.contentType(MediaType.parseMediaType(podcast.getContentType()))
//				.body(new InputStreamResource(podcast.getInputStream()));
//	}

	@PostMapping
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> uploadPodcast(@RequestBody @Valid  PodcastUrl podcastUrl) throws IOException, InvalidFeedException, MalformedFeedException, DateFormatException {

		service.uploadFeed(podcastUrl);

		return  ResponseEntity.ok().body(null);
	}
}
