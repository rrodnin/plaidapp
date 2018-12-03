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
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RssService {

	@Autowired
	RssFeedRepository repository;

	@Autowired
	ContentService service;

	@Autowired
	TopicService topicService;

	public RssFeed createRss(PodcastUrl podcastUrl) throws IOException, FeedException {
		if(repository.existsByUrl(podcastUrl.getUrl())) {
			throw new PodcastAlreadyExistsException();
		}

		URL url = new URL(podcastUrl.getUrl());

		SyndFeedInput input = new SyndFeedInput();
		SyndFeed rssfeed = input.build(new XmlReader(url));

		if (!(podcastUrl.getTopicId().length == 0)) {
			for (String topicId : podcastUrl.getTopicId()) {
				topicService.validateTopicId(topicId);
			}
		}
		RssFeed feed = new RssFeed();
		feed.setUrl(podcastUrl.getUrl());
		feed.setTopics(podcastUrl.getTopicId());

		repository.save(feed);

		for (SyndEntry entry : (List<SyndEntry>) rssfeed.getEntries()) {
			try {
				Content content = new Content();
				content.setType(Type.WEBPAGE);
				content.setGuid(entry.getUri());
				content.setTitle(entry.getTitle());
				content.setDescription(rssfeed.getTitle());

				List<SyndCategoryImpl> categories = (List<SyndCategoryImpl>) entry.getCategories();
				Set<String> collect = categories.stream().map(syndCategory -> syndCategory.getName()).collect(Collectors.toSet());
				content.setCategories(collect);
				List<SyndContentImpl> textList = (List<SyndContentImpl>) entry.getContents();
				SyndContentImpl text;
				SyndContentImpl descr = (SyndContentImpl) entry.getDescription();
				if (textList.size() != 0) {
					text= textList.get(0);
				} else if(descr != null) {
					text = descr;
				} else {
					text = new SyndContentImpl();
					text.setValue("");
				}
				content.setText(text.getValue());
				content.setUrl(entry.getLink());
				content.setCreatedAt(entry.getPublishedDate());


				Image image = new Image();
				Document doc = Jsoup.connect(entry.getLink()).get();
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
			} catch (IOException e) {
				continue;
			}

		}
		return feed;
	}

	@Scheduled(fixedRate = 600000)
	public void updateFeeds()  {
		List<RssFeed> all = repository.findAll();

		for (RssFeed feed : all) {
			URL url = null;
			try {
				url = new URL(feed.getUrl());
			} catch (MalformedURLException e) {
				continue;
			}
			Podcast podcast;
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed rssfeed = null;
			try {
				rssfeed = input.build(new XmlReader(url));
			} catch (FeedException | IOException e) {
				continue;
			}
			for (SyndEntry entry : (List<SyndEntry>) rssfeed.getEntries()) {
				try {
					if (service.existsByGuid(entry.getUri()))
						break;
					Content content = new Content();
					content.setType(Type.WEBPAGE);
					content.setGuid(entry.getUri());
					content.setTitle(entry.getTitle());
					content.setDescription(rssfeed.getTitle());

					List<SyndCategoryImpl> categories = (List<SyndCategoryImpl>) entry.getCategories();
					Set<String> collect = categories.stream().map(syndCategory -> syndCategory.getName()).collect(Collectors.toSet());
					content.setCategories(collect);
					List<SyndContentImpl> textList = (List<SyndContentImpl>) entry.getContents();
					SyndContentImpl descr = (SyndContentImpl) entry.getDescription();
					SyndContentImpl text;
					if (textList.size() != 0) {
						text= textList.get(0);
					} else if(descr != null) {
						text = descr;
					} else {
						text = new SyndContentImpl();
						text.setValue("");
					}
					content.setText(text.getValue());
					content.setUrl(entry.getLink());
					content.setCreatedAt(entry.getPublishedDate());


					Image image = new Image();
					Document doc = Jsoup.connect(entry.getLink()).get();
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
				} catch (IOException e) {
					continue;
				}

			}
		}
	}



}
