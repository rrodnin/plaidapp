package com.aradata.plaidapp.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class YoutubeVideo extends Content{

	@Override
	public String toString() {
		return "YoutubeVideo{" +
				"id='" + id + '\'' +
				", type='" + type + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", url='" + url + '\'' +
				", likes=" + likes +
				'}';
	}
}
