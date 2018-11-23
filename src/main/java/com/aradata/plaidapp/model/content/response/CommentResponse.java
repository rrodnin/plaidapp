package com.aradata.plaidapp.model.content.response;

import com.aradata.plaidapp.model.content.Comment;
import com.aradata.plaidapp.model.content.Type;

import java.util.Date;

public class CommentResponse {

	private String id;
	private String text;
	private String contentId;
	private Type contentType;
	private String ownerId;
	private Date createdAt;

	public static CommentResponse createByComment(Comment comment) {
		CommentResponse response = new CommentResponse();

		response.setId(comment.getId());
		response.setText(comment.getText());
		response.setContentId(comment.getContentId());
		response.setCreatedAt(comment.getCreatedAt());
		response.setContentType(comment.getType());
		response.setOwnerId(comment.getOwnerId());
		return response;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public Type getContentType() {
		return contentType;
	}

	public void setContentType(Type contentType) {
		this.contentType = contentType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
