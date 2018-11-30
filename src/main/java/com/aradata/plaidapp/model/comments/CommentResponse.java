package com.aradata.plaidapp.model.comments;

import org.springframework.hateoas.ResourceSupport;

import java.util.Date;
import java.util.List;

public class CommentResponse extends ResourceSupport {

	private String commentId;
	private String contentId;
	private String ownerId;
	private String text;
	private Date createdAt;

	public boolean isReply() {
		return isReply;
	}

	public void setReply(boolean reply) {
		isReply = reply;
	}

	public List<Comment> getReplies() {
		return replies;
	}

	public void setReplies(List<Comment> replies) {
		this.replies = replies;
	}

	private boolean isReply;

	private List<Comment> replies;

	private String ownerName;

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public static CommentResponse createFromComment(Comment comment) {
		CommentResponse response = new CommentResponse();
		response.setCommentId(comment.getId());
		response.setContentId(comment.getContentId());
		response.setCreatedAt(comment.getCreatedAt());
		response.setOwnerId(comment.getOwnerId());
		response.setText(comment.getText());
		response.replies = comment.getReplies();
		response.isReply = comment.isReply();
		return response;
	}



	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
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
