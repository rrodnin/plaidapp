package com.aradata.plaidapp.service.content;

import com.aradata.plaidapp.exception.ResourceNotFoundException;
import com.aradata.plaidapp.model.Topic;
import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.request.TopicRequest;
import com.aradata.plaidapp.model.content.response.ContentResponse;
import com.aradata.plaidapp.model.payloads.PagedResponse;
import com.aradata.plaidapp.repository.TopicRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TopicService {

	@Autowired
	private TopicRepository repository;

	@Autowired
	private ContentService contentService;

	public String createTopic(TopicRequest request) {
		Topic topic = new Topic();
		topic.setName(request.getName());
		Topic save = repository.save(topic);
		return save.getId();
	}

	public void addContentToTopic(String topicId, String contentId) {
		Content content = contentService.validateContentId(contentId);
		if(!repository.existsById(topicId)) {
			throw new ResourceNotFoundException("topic", "id", topicId);
		}

		Topic byId = repository.findById(topicId).get();
		byId.addContent(contentId);
		repository.save(byId);
	}

	public PagedResponse<ContentResponse> getContentByTopicId(String topicId, int page, int size, UserPrincipal currentUser) {
		if(!repository.existsById(topicId)) {
			throw new ResourceNotFoundException("topic", "id", topicId);
		}
		Topic topic = repository.findById(topicId).get();
		return contentService.findAllById(topic.getContent(), page, size, currentUser);
	}

	public List<Topic> fetchAll() {
		return repository.findAll();
	}
}
