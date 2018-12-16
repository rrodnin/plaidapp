package com.aradata.plaidapp.service.content;

import com.aradata.plaidapp.exception.ContentIsNotPodcastException;
import com.aradata.plaidapp.exception.PodcastAlreadyExistsException;
import com.aradata.plaidapp.exception.ResourceNotFoundException;
import com.aradata.plaidapp.model.content.*;
import com.aradata.plaidapp.model.content.request.PodcastUrl;
import com.aradata.plaidapp.repository.PodcastFeedRepository;
import com.icosillion.podengine.exceptions.DateFormatException;
import com.icosillion.podengine.exceptions.InvalidFeedException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.Podcast;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@EnableScheduling
@Service
public class PodcastService {

	@Autowired
	private ContentService service;

	@Autowired
	private TopicService topicService;

	@Autowired
	private GridFsOperations operations;

	@Autowired
	private PodcastFeedRepository repository;

	public void uploadFeed(PodcastUrl podcastUrl) throws MalformedURLException, InvalidFeedException, MalformedFeedException, DateFormatException {
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
		PodcastFeed feed = new PodcastFeed();
		feed.setUrl(podcastUrl.getUrl());
		feed.setTopics(podcastUrl.getTopicId());

		repository.save(feed);

		for (Episode episode: podcast.getEpisodes()) {

			Content content = new Content();
			content.setType(Type.PODCAST);
			content.setCategories(episode.getCategories());
			content.setGuid(episode.getGUID());
			content.setTitle(episode.getTitle());
			content.setDescription(episode.getAuthor());
			content.setText(episode.getDescription());
			content.setUrl(episode.getEnclosure().getURL().toString());
			content.setCreatedAt(episode.getPubDate());

			Image image = new Image();

			try {
				image.setUrl(episode.getITunesInfo().getImage().toString());
			} catch (Exception e) {
				try {
					image.setUrl(podcast.getImageURL().toString());
				} catch (Exception e1) {
					image.setUrl("");
				}
			}



			content.addImage(image);
			Content saved = service.saveContent(content);

			if (!(feed.getTopics().length == 0)) {
				for (String topicId : feed.getTopics()) {
					topicService.addContentToTopic(topicId, saved.getId());
				}
			}

		}


	}


	@Scheduled(fixedRate = 600000)
	public void updateFeeds()  {
		List<PodcastFeed> all = repository.findAll();

		for (PodcastFeed feed : all) {
			Podcast podcast;
			try {
				URL url = new URL(feed.getUrl());
				podcast = new Podcast(url);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			for (Episode episode : podcast.getEpisodes()) {
				try {
					System.out.println(episode.getGUID());
					if (service.existsByGuid(episode.getGUID()))
						break;
					Content content = new Content();
					content.setType(Type.PODCAST);
					content.setGuid(episode.getGUID());
					content.setTitle(episode.getTitle());
					content.setDescription(episode.getAuthor());
					content.setCategories(episode.getCategories());
					content.setText(episode.getDescription());
					content.setUrl(episode.getEnclosure().getURL().toString());
					content.setCreatedAt(episode.getPubDate());

					Image image = new Image();
					try {
						image.setUrl(episode.getITunesInfo().getImage().toString());
					} catch (Exception e) {
						image.setUrl(podcast.getImageURL().toString());
					}

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
		}
	}





	//DEPPRECATED

//	public String store(MultipartFile file, String contentId) throws IOException {
//		Content contentById = service.getContentById(contentId);
//		if (contentById.getType() != Type.PODCAST)
//			throw new ContentIsNotPodcastException();
//		DBObject metaData = new BasicDBObject();
//		((BasicDBObject) metaData).put("type", "podcast");
//		((BasicDBObject) metaData).put("contentId", contentId);
//		ObjectId store = operations.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metaData);
//		return store.toHexString();
//	}
//
//	public GridFsResource getPodcastById(String imageId) {
//		GridFSFile id = operations.findOne(new Query(Criteria.where("_id").is(imageId)));
//		if(id == null) {
//			throw new ResourceNotFoundException("podcast", "id", imageId);
//		}
//		return operations.getResource(id);
//	}
//
//	public String getPodcastIdByContentId(String contentId) {
//		GridFSFile one = operations.findOne(new Query(Criteria.where("metadata.contentId").is(contentId)));
//		if (one == null) {
//			return "";
//		}
//		return one.getObjectId().toHexString();
//	}
}
