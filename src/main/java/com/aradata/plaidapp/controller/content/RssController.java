package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.model.content.request.PodcastUrl;
import com.aradata.plaidapp.service.content.RssService;
import com.icosillion.podengine.exceptions.InvalidFeedException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.sun.syndication.io.FeedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/rss")
public class RssController {

	@Autowired
	private RssService service;

	@PostMapping
	public ResponseEntity<?> uploadRss(@RequestBody @Valid  PodcastUrl url) throws MalformedFeedException, InvalidFeedException, IOException, FeedException {

		return ResponseEntity.ok(service.createRss(url));
	}
}
