package com.aradata.plaidapp.service.content;

import com.aradata.plaidapp.exception.PodcastAlreadyExistsException;
import com.aradata.plaidapp.model.content.*;
import com.aradata.plaidapp.model.content.request.PodcastUrl;
import com.aradata.plaidapp.repository.RssFeedRepository;
import com.icosillion.podengine.exceptions.DateFormatException;
import com.icosillion.podengine.exceptions.InvalidFeedException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.Podcast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class RssService {

	@Autowired
	RssFeedRepository repository;

	@Autowired
	ContentService service;

	@Autowired
	TopicService topicService;

	public RssFeed createRss(PodcastUrl podcastUrl) throws InvalidFeedException, MalformedFeedException, IOException {
		if(repository.existsByUrl(podcastUrl.getUrl())) {
			throw new PodcastAlreadyExistsException();
		}

		URL url = new URL(podcastUrl.getUrl());

		Podcast podcast = new Podcast(url);

		if (!(podcastUrl.getTopicId().length == 0)) {
			for (String topicId : podcastUrl.getTopicId()) {
				topicService.validateTopicId(topicId);
			}
		}
		RssFeed feed = new RssFeed();
		feed.setUrl(podcastUrl.getUrl());
		feed.setTopics(podcastUrl.getTopicId());

		repository.save(feed);

		for (Episode episode : podcast.getEpisodes()) {
			try {
				Content content = new Content();
				content.setType(Type.WEBPAGE);
				content.setGuid(episode.getGUID());
				content.setTitle(episode.getTitle());
				content.setDescription(podcast.getTitle());
				content.setCategories(episode.getCategories());
				content.setText(episode.getDescription());
				content.setUrl(episode.getGUID());
				content.setCreatedAt(episode.getPubDate());

				Image image = new Image();
				Document doc = Jsoup.connect(episode.getGUID()).get();
				String imageUrl = null;
				Elements metaOgImage = doc.select("meta[property=og:image]");
				if (metaOgImage!=null) {
					imageUrl = metaOgImage.attr("content");
				}

				image.setUrl(imageUrl);
				content.addImage(image);
				Content saved = service.saveContent(content);

				if (!(feed.getTopics().length == 0)) {
					for (String topicId : feed.getTopics()) {
						topicService.addContentToTopic(topicId, saved.getId());
					}
				}
			} catch (MalformedFeedException | DateFormatException | MalformedURLException e) {
				continue;
			}

		}
		return feed;
	}

	@Scheduled(fixedRate = 600000)
	public void updateFeeds()  {
		List<RssFeed> all = repository.findAll();

		for (RssFeed feed : all) {
			Podcast podcast;
			try {
				podcast = new Podcast(feed.getUrl());
			} catch (MalformedFeedException e) {
				continue;
			}
			for (Episode episode : podcast.getEpisodes()) {
				try {
					if (service.existsByGuid(episode.getGUID()))
						break;
					Content content = new Content();
					content.setType(Type.WEBPAGE);
					content.setGuid(episode.getGUID());
					content.setTitle(episode.getTitle());
					content.setDescription(podcast.getTitle());
					content.setCategories(episode.getCategories());
					content.setText(episode.getDescription());
					content.setUrl(episode.getGUID());
					content.setCreatedAt(episode.getPubDate());

					Image image = new Image();
					Document doc = Jsoup.connect(episode.getGUID()).get();
					String imageUrl = null;
					Elements metaOgImage = doc.select("meta[property=og:image]");
					if (metaOgImage!=null) {
						imageUrl = metaOgImage.attr("content");
					}

					image.setUrl(imageUrl);
					content.addImage(image);
					Content saved = service.saveContent(content);

					if (!(feed.getTopics().length == 0)) {
						for (String topicId : feed.getTopics()) {
							topicService.addContentToTopic(topicId, saved.getId());
						}
					}
				} catch (MalformedFeedException | DateFormatException | MalformedURLException e) {
					continue;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}



}
