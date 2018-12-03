package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.request.QueryRequest;
import com.aradata.plaidapp.model.content.request.SaveContentByVideoIdRequest;
import com.aradata.plaidapp.model.content.request.SavePlaylistRequest;
import com.aradata.plaidapp.service.YoutubeService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/youtube")
public class YoutubeController {

	@Autowired
	YoutubeService service;

	@PostMapping
	public ResponseEntity<?> addVideo(@RequestBody @Valid SaveContentByVideoIdRequest request) throws IOException {
		Content content = service.saveContentByVideoId(request);

		return ResponseEntity.ok(content);
	}

	@PostMapping("/playlists")
	public ResponseEntity<?> addPlaylist(@RequestBody @Valid SavePlaylistRequest request) throws IOException {

		return ResponseEntity.ok(service.savePlaylist(request));
	}

	@PostMapping("/queries")
	public ResponseEntity<?> addQuery(@RequestBody QueryRequest query) {

		return ResponseEntity.ok(service.addQuery(query));
	}

	@PostMapping("/updateQueries")
	public ResponseEntity<?> updateQueries() throws IOException {
		service.updateQueries();
		return ResponseEntity.ok().body(null);
	}

	@PostMapping("/updatePlaylists")
	public ResponseEntity<?> updatePlaylist() throws IOException {
		service.updatePlaylists();
		return ResponseEntity.ok().body(null);
	}

	public ResponseEntity<?> updateVideos() throws IOException {
		service.updateVideos();
		return ResponseEntity.ok().body(null);
	}
}
