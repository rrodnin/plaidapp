package com.aradata.plaidapp.model;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Podcast extends Content {


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

}
