package com.aradata.plaidapp.model.content;

import java.math.BigInteger;

public class Extra {

	private int podcastListening;
	private BigInteger youtubeViews;
	private BigInteger youtubeLikes;
	private BigInteger youtubeDislikes;

	private int views;


	public BigInteger getYoutubeViews() {
		return youtubeViews;
	}

	public void setYoutubeViews(BigInteger youtubeViews) {
		this.youtubeViews = youtubeViews;
	}

	public BigInteger getYoutubeLikes() {
		return youtubeLikes;
	}

	public BigInteger getYoutubeDislikes() {
		return youtubeDislikes;
	}

	public void setYoutubeDislikes(BigInteger youtubeDislikes) {
		this.youtubeDislikes = youtubeDislikes;
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



	public void setYoutubeLikes(BigInteger youtubeLikes) {
		this.youtubeLikes = youtubeLikes;
	}
}
