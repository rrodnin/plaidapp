package com.aradata.plaidapp.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Podcast {

	@Id
	private String id;

	private int likes;
	private String url;

	@NotNull
	@Size(min = 2, max = 20)
	private String name;

	@NotNull
	private String description;

	@Override
	public String toString() {
		return "Podcast{" +
				"id='" + id + '\'' +
				", likes=" + likes +
				", url='" + url + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
