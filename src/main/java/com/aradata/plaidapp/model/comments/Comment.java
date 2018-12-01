package com.aradata.plaidapp.model.comments;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Comment {

	@Id
	private String id;

	private String contentId;

	private String ownerId;

	private boolean isReply = false;

	private String replyTo;

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	private String ownerName;

	public boolean isReply() {
		return isReply;
	}

	public void setReply(boolean reply) {
		isReply = reply;
	}

	private String text;
	private Date createdAt = new Date();

	private List<Comment> replies;

	public void addReply(Comment comment) {
		if (replies == null)
			replies = new LinkedList<>();
		replies.add(comment);
	}

	public List<Comment> getReplies() {
		if (replies ==null)
			replies = new LinkedList<>();
		return replies;
	}

	public void setReplies(List<Comment> replies) {
		this.replies = replies;
	}

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

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
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
}
