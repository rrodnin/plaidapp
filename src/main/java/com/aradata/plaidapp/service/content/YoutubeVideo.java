package com.aradata.plaidapp.service.content;

import org.springframework.data.annotation.Id;

public class YoutubeVideo {

	@Id
	private String id;

	private String youtubeId;

	private String contentId;


	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getYoutubeId() {
		return youtubeId;
	}

	public void setYoutubeId(String youtubeId) {
		this.youtubeId = youtubeId;
	}
}
