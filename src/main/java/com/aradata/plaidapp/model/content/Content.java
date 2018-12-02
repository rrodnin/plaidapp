package com.aradata.plaidapp.model.content;

import org.springframework.data.annotation.Id;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedList;
import java.util.Set;

public class Content {

	@Id
	protected String id;

	@NotNull
	@NotBlank
	protected String title;

	@NotNull
	protected Type type;

	@NotNull
	@NotBlank
	protected String url;

	@NotNull
	@NotBlank
	protected String description;

	@NotNull
	@NotBlank
	protected String text;

	protected Extra extra = new Extra();

	protected String guid;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	private Set<String> categories;

	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	protected int likes = 0;

	protected LinkedList<Image> images;

	public Extra getExtra() {
		return extra;
	}

	public void setExtra(Extra extra) {
		this.extra = extra;
	}

	public LinkedList<Image> getImages() {
		if (images == null)
			images = new LinkedList<>();
		return images;
	}

	public void setImages(LinkedList<Image> images) {
		this.images = images;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	protected Date createdAt = new Date();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void addImage(Image image) {
		if (images == null)
			images = new LinkedList<>();
		images.add(image);
	}
}
