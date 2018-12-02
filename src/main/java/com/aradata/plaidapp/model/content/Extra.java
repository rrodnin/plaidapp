package com.aradata.plaidapp.model.content;

public class Extra {

	private int podcastListening;
	private int youtubeViews;
	private int youtubeLikes;
	private int youtubeDislikes;
	private String podcastUrl;

	private int views;



	public int getYoutubeDislikes() {
		return youtubeDislikes;
	}

	public void setYoutubeDislikes(int youtubeDislikes) {
		this.youtubeDislikes = youtubeDislikes;
	}


	public String getPodcastUrl() {
		return podcastUrl;
	}

	public void setPodcastUrl(String podcastUrl) {
		this.podcastUrl = podcastUrl;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getPodcastListening() {
		return podcastListening;
	}

	public void setPodcastListening(int podcastListening) {
		this.podcastListening = podcastListening;
	}

	public int getYoutubeViews() {
		return youtubeViews;
	}

	public void setYoutubeViews(int youtubeViews) {
		this.youtubeViews = youtubeViews;
	}

	public int getYoutubeLikes() {
		return youtubeLikes;
	}

	public void setYoutubeLikes(int youtubeLikes) {
		this.youtubeLikes = youtubeLikes;
	}
}
