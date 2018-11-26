package com.aradata.plaidapp.model;

import com.aradata.plaidapp.model.content.Content;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.LinkedList;
import java.util.List;

public class Topic {

	protected String id;
	protected String name;

	@JsonIgnore
	protected LinkedList<String> content;

	public LinkedList<String> getContent() {
		return content;
	}

	public void setContent(LinkedList<String> content) {
		this.content = content;
	}

	public void addContent(String contentId) {
		if(content == null)
			content = new LinkedList<>();
		content.add(contentId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
