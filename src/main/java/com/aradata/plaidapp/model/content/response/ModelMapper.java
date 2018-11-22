package com.aradata.plaidapp.model.content.response;

import com.aradata.plaidapp.model.content.YoutubeContent;

public class ModelMapper {

	public static YoutubeContentResponse mapYoutubeToYoutubeResponse(YoutubeContent content) {
		YoutubeContentResponse response = new YoutubeContentResponse();
		response.setId(content.getId());
		response.setDescription(content.getDescription());
		response.setName(content.getName());
		response.setType(content.getType());
		response.setUrl(content.getUrl());
		return response;
	}
}
