package com.aradata.plaidapp.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class YoutubeVideo {

	@Id
	private String id;

	@NotNull
	@Size(min = 3, max = 20)
	private String name;

	@NotNull
	private String description;
	private int likes;

	@NotNull
	private String url;

	@Override
	public String toString() {
		return "YoutubeVideo{" +
				"id='" + id + '\'' +
				", videoName='" + name + '\'' +
				", videoDescription='" + description + '\'' +
				", likes=" + likes +
				", url='" + url + '\'' +
				'}';
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
}
