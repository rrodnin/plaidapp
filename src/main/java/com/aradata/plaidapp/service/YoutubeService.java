package com.aradata.plaidapp.service;

import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.Extra;
import com.aradata.plaidapp.model.content.Image;
import com.aradata.plaidapp.model.content.Type;
import com.aradata.plaidapp.model.content.request.SaveContentByVideoIdRequest;
import com.aradata.plaidapp.model.content.request.SavePlaylistRequest;
import com.aradata.plaidapp.repository.YoutubePlaylistRepository;
import com.aradata.plaidapp.repository.YoutubeVideoRepository;
import com.aradata.plaidapp.service.content.ContentService;
import com.aradata.plaidapp.service.content.TopicService;
import com.aradata.plaidapp.service.content.YoutubePlaylist;
import com.aradata.plaidapp.service.content.YoutubeVideo;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
			Content content = new Content();

			content.setUrl("https://www.youtube.com/watch?v=" + item.getId());
			content.setTitle(item.getSnippet().getTitle());
			content.setType(Type.YOUTUBE);
			content.setDescription(item.getSnippet().getChannelTitle());
			content.setText(item.getSnippet().getDescription());
			Image image = new Image();
			image.setUrl(item.getSnippet().getThumbnails().getHigh().getUrl());
			content.addImage(image);

			VideoStatistics statistics = getStatisticsById(item.getSnippet().getResourceId().getVideoId());
			content.getExtra().setYoutubeLikes(statistics.getLikeCount());
			content.getExtra().setYoutubeDislikes(statistics.getDislikeCount());
			content.getExtra().setYoutubeViews(statistics.getViewCount());

			Content content1 = service.saveContent(content);

			YoutubeVideo youtubeVideo = new YoutubeVideo();
			youtubeVideo.setYoutubeId(item.getId());
			youtubeVideo.setContentId(content1.getId());
			repository.save(youtubeVideo);
			for (String topicId : playlist1.getTopics()) {
				topicService.addContentToTopic(topicId, content1.getId());
			}
		}

		return playlist1;
	}

	@Scheduled(fixedRate = 6000000)
	public void updatePlaylists() throws IOException {
		List<YoutubePlaylist> all = playlistRepository.findAll();

		for (YoutubePlaylist playlist: all) {
			YouTube.PlaylistItems.List request = youtube.playlistItems().list("snippet,contentDetails");
			request.setId(playlist.getListId());
			request.setKey("AIzaSyBr8iJMOG76deZiPAYlwLXdNnkaLa6KE6I");
			PlaylistItemListResponse execute = request.execute();

			for (PlaylistItem item : execute.getItems()) {
				if (repository.existsByYoutubeId(item.getId()))
					break;
				Content content = new Content();
				content.setUrl("https://www.youtube.com/watch?v=" + item.getId());
				content.setTitle(item.getSnippet().getTitle());
				content.setType(Type.YOUTUBE);
				content.setDescription(item.getSnippet().getChannelTitle());
				content.setText(item.getSnippet().getDescription());
				Image image = new Image();
				image.setUrl(item.getSnippet().getThumbnails().getHigh().getUrl());
				content.addImage(image);

				VideoStatistics statistics = getStatisticsById(item.getSnippet().getResourceId().getVideoId());
				content.getExtra().setYoutubeLikes(statistics.getLikeCount());
				content.getExtra().setYoutubeDislikes(statistics.getDislikeCount());
				content.getExtra().setYoutubeViews(statistics.getViewCount());

				Content content1 = service.saveContent(content);

				YoutubeVideo youtubeVideo = new YoutubeVideo();
				youtubeVideo.setYoutubeId(item.getId());
				youtubeVideo.setContentId(content1.getId());
				repository.save(youtubeVideo);
				for (String topicId : playlist.getTopics()) {
					topicService.addContentToTopic(topicId, content1.getId());
				}

			}
		}
	}

	public VideoStatistics getStatisticsById(String id) throws IOException {
		YouTube.Videos.List videos = youtube.videos().list("snippet,contentDetails,statistics");
		videos.setId(id);
		videos.setKey("AIzaSyBr8iJMOG76deZiPAYlwLXdNnkaLa6KE6I");
		VideoListResponse execute = videos.execute();
		return execute.getItems().get(0).getStatistics();
	}



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




	@Scheduled(fixedRate = 600000)
	public void updateVideos() throws IOException {
		List<YoutubeVideo> all = repository.findAll();

		for (YoutubeVideo video: all) {
			Content content = service.findById(video.getContentId());
			YouTube.Videos.List videos = youtube.videos().list("snippet,contentDetails,statistics");
			videos.setId(video.getYoutubeId());
			videos.setKey("AIzaSyBr8iJMOG76deZiPAYlwLXdNnkaLa6KE6I");
			VideoListResponse execute = videos.execute();
			Video update = execute.getItems().get(0);

			content.getExtra().setYoutubeViews(update.getStatistics().getViewCount());
			content.getExtra().setYoutubeLikes(update.getStatistics().getLikeCount());
			content.getExtra().setYoutubeDislikes(update.getStatistics().getDislikeCount());

			service.saveContent(content);

		}
	}
}
