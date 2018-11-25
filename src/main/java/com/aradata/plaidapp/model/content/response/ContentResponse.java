package com.aradata.plaidapp.model.content.response;

import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.Image;
import com.aradata.plaidapp.model.content.Type;
import org.springframework.data.annotation.Id;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;

public class ContentResponse extends ResourceSupport {

	protected String contentId;

	protected String title;
	protected Type type;
	protected String url;
	protected String description;
	protected String text;
	protected int likes = 0;
	protected LinkedList<Image> images;

	protected boolean userLikes;

	public static ContentResponse createFromContent(Content content) {
		ContentResponse response = new ContentResponse();
		response.contentId = content.getId();
		response.title = content.getTitle();
		response.type = content.getType();
		response.url = content.getUrl();
		response.description = content.getDescription();
		response.text = content.getText();
		response.likes = content.getLikes();
		response.images = content.getImages();
		return response;
	}

	public boolean isUserLikes() {
		return userLikes;
	}

	public void setUserLikes(boolean userLikes) {
		this.userLikes = userLikes;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public LinkedList<Image> getImages() {
		return images;
	}

	public void setImages(LinkedList<Image> images) {
		this.images = images;
	}
}
