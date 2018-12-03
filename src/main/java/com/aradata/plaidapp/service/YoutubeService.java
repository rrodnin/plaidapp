package com.aradata.plaidapp.service;

import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.Extra;
import com.aradata.plaidapp.model.content.Image;
import com.aradata.plaidapp.model.content.Type;
import com.aradata.plaidapp.model.content.request.QueryRequest;
import com.aradata.plaidapp.model.content.request.SaveContentByVideoIdRequest;
import com.aradata.plaidapp.model.content.request.SavePlaylistRequest;
import com.aradata.plaidapp.repository.QueryRepository;
import com.aradata.plaidapp.repository.YoutubePlaylistRepository;
import com.aradata.plaidapp.repository.YoutubeVideoRepository;
import com.aradata.plaidapp.service.content.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@EnableScheduling
@Service
public class YoutubeService {

	private final JsonFactory JSON_FACTORY =
			JacksonFactory.getDefaultInstance();

	private HttpTransport HTTP_TRANSPORT;

	private  YouTube youtube;

	@Autowired
	private ContentService service;

	@Autowired
	private TopicService topicService;

	@Autowired
	private YoutubeVideoRepository repository;

	@Autowired
	private YoutubePlaylistRepository playlistRepository;

	@Autowired
	private QueryRepository queryRepository;

	DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			.withZone(ZoneId.of("UTC"));


	@PostConstruct
	public void init() {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {
				}
			}).setApplicationName("Directory").build();
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}


	public YoutubePlaylist savePlaylist(SavePlaylistRequest request) throws IOException {
		if (playlistRepository.existsByListId(request.getId()))
			throw new IllegalArgumentException("playlist exists");
		YouTube.PlaylistItems.List playlist = youtube.playlistItems().list("snippet,contentDetails");
		playlist.setPlaylistId(request.getId());
		playlist.setKey("AIzaSyBr8iJMOG76deZiPAYlwLXdNnkaLa6KE6I");
		playlist.setMaxResults(50L);
		PlaylistItemListResponse execute = playlist.execute();

		YoutubePlaylist playlist1 = new YoutubePlaylist();
		playlist1.setListId(request.getId());
		if (request.getTopics() != null) {
			for (String topicId : request.getTopics()) {
				topicService.validateTopicId(topicId);
			}
			playlist1.setTopics(request.getTopics());
		}
		playlistRepository.save(playlist1);

		LinkedList<PlaylistItem> items = new LinkedList<>(execute.getItems());

		String nextPage  = execute.getNextPageToken();

		while (nextPage != null) {
			playlist.setPageToken(nextPage);
			execute = playlist.execute();
			items.addAll(execute.getItems());
			nextPage = execute.getNextPageToken();
		}

		for (PlaylistItem item : items) {

			SaveContentByVideoIdRequest request1 = new SaveContentByVideoIdRequest();
			request1.setId(item.getSnippet().getResourceId().getVideoId());
			request1.setTopics(playlist1.getTopics());

			saveContentByVideoId(request1);
		}

		return playlist1;
	}

//	@Scheduled(fixedRate = 6000000)
	public void updatePlaylists() throws IOException {
		List<YoutubePlaylist> all = playlistRepository.findAll();

		for (YoutubePlaylist playlist: all) {
			YouTube.PlaylistItems.List request = youtube.playlistItems().list("snippet,contentDetails");
			request.setId(playlist.getListId());
			request.setKey("AIzaSyBr8iJMOG76deZiPAYlwLXdNnkaLa6KE6I");
			request.setMaxResults(50L);
			PlaylistItemListResponse execute = request.execute();


			for (PlaylistItem item : execute.getItems()) {
				if (repository.existsByYoutubeId(item.getSnippet().getResourceId().getVideoId()))
					break;
				SaveContentByVideoIdRequest request1 = new SaveContentByVideoIdRequest();
				request1.setTopics(playlist.getTopics());
				request1.setId(item.getSnippet().getResourceId().getVideoId());
				saveContentByVideoId(request1);
			}
		}
	}

//	public VideoStatistics getStatisticsById(String id) throws IOException {
//		YouTube.Videos.List videos = youtube.videos().list("snippet,contentDetails,statistics");
//		videos.setId(id);
//		videos.setKey("AIzaSyBr8iJMOG76deZiPAYlwLXdNnkaLa6KE6I");
//		VideoListResponse execute = videos.execute();
//		return execute.getItems().get(0).getStatistics();
//	}



	public Content saveContentByVideoId(SaveContentByVideoIdRequest request) throws IOException {

		YouTube.Videos.List videos = youtube.videos().list("snippet,contentDetails,statistics");
		videos.setId(request.getId());
		videos.setKey("AIzaSyBr8iJMOG76deZiPAYlwLXdNnkaLa6KE6I");
		VideoListResponse execute = videos.execute();
		Video video = execute.getItems().get(0);

		if (!(request.getTopics().length == 0)) {
			for (String topicId : request.getTopics()) {
				topicService.validateTopicId(topicId);
			}
		}

		Content content = new Content();

		content.setUrl("https://www.youtube.com/watch?v=" + request.getId());
		content.setTitle(video.getSnippet().getTitle());
		content.setType(Type.YOUTUBE);
		content.setDescription(video.getSnippet().getChannelTitle());
		content.setText(video.getSnippet().getDescription());
		Image image = new Image();
		image.setUrl(video.getSnippet().getThumbnails().getHigh().getUrl());
		content.addImage(image);
		content.getExtra().setYoutubeLikes(video.getStatistics().getLikeCount());
		content.getExtra().setYoutubeDislikes(video.getStatistics().getDislikeCount());
		content.getExtra().setYoutubeViews(video.getStatistics().getViewCount());
		content.setCreatedAt(new Date(video.getSnippet().getPublishedAt().getValue()));
		if (video.getSnippet().getTags() != null) {
			content.setCategories(new HashSet<>(video.getSnippet().getTags()));
		}

		Content content1 = service.saveContent(content);

		YoutubeVideo youtubeVideo = new YoutubeVideo();
		youtubeVideo.setYoutubeId(request.getId());
		youtubeVideo.setContentId(content1.getId());
		repository.save(youtubeVideo);
		for (String topicId : request.getTopics()) {
			topicService.addContentToTopic(topicId, content1.getId());
		}

		return content1;
	}



//	@Scheduled(fixedRate = 5000000)
	public void updatePubDate() throws IOException {
		List<YoutubeVideo> all = repository.findAll();

		for (YoutubeVideo video: all) {
			Content content = service.findById(video.getContentId());
			YouTube.Videos.List videos = youtube.videos().list("snippet,contentDetails,statistics");
			videos.setId(video.getYoutubeId());
			videos.setKey("AIzaSyBr8iJMOG76deZiPAYlwLXdNnkaLa6KE6I");
			videos.setMaxResults(50L);
			VideoListResponse execute = videos.execute();
			Video update = execute.getItems().get(0);
			content.setCreatedAt(new Date(execute.getItems().get(0).getSnippet().getPublishedAt().getValue()));

			service.saveContent(content);

		}
	}

//	@Scheduled(fixedRate = 600000)
	public void updateVideos() throws IOException {
		List<YoutubeVideo> all = repository.findAll();

		for (YoutubeVideo video: all) {
			Content content = service.findById(video.getContentId());
			YouTube.Videos.List videos = youtube.videos().list("snippet,contentDetails,statistics");
			videos.setId(video.getYoutubeId());
			videos.setKey("AIzaSyBr8iJMOG76deZiPAYlwLXdNnkaLa6KE6I");
			videos.setMaxResults(50L);
			VideoListResponse execute = videos.execute();
			Video update = execute.getItems().get(0);

			content.getExtra().setYoutubeViews(update.getStatistics().getViewCount());
			content.getExtra().setYoutubeLikes(update.getStatistics().getLikeCount());
			content.getExtra().setYoutubeDislikes(update.getStatistics().getDislikeCount());

			service.saveContent(content);

		}
	}

	public void updateQueries() throws IOException {

		List<Query> all = queryRepository.findAll();

		for (Query query : all) {
			String q = query.getQuery();

			YouTube.Search.List list = youtube.search().list("id, snippet");
			list.setKey("AIzaSyBr8iJMOG76deZiPAYlwLXdNnkaLa6KE6I");
			list.setQ(q);
			list.setType("video");
			list.setMaxResults(50L);

			SearchListResponse execute = list.execute();

			List<SearchResult> items = execute.getItems();

			String nextPage  = execute.getNextPageToken();

			for (int i = 0; i < 80; i++) {
				list.setPageToken(nextPage);
				execute = list.execute();
				items.addAll(execute.getItems());
				nextPage = execute.getNextPageToken();
			}

			for (SearchResult item : items) {
				if (repository.existsByYoutubeId(item.getId().getVideoId()))
					break;

				SaveContentByVideoIdRequest request = new SaveContentByVideoIdRequest();
				request.setTopics(query.getTopics());
				request.setId(item.getId().getVideoId());

				saveContentByVideoId(request);
			}

		}

	}


	public Query addQuery(QueryRequest query) {
		Query query1 = new Query();
		query1.setQuery(query.getQuery());
		query1.setTopics(query.getTopics());
		return queryRepository.save(query1);
	}
}
