package com.aradata.plaidapp.service.content;

import com.aradata.plaidapp.exception.ResourceNotFoundException;
import com.aradata.plaidapp.model.content.Comment;
import com.aradata.plaidapp.model.content.request.CommentRequest;
import com.aradata.plaidapp.model.content.response.CommentResponse;
import com.aradata.plaidapp.repository.CommentRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

	@Autowired
	private CommentRepository repository;

	public Comment createComment(CommentRequest request, UserPrincipal currentUser) {
		Comment comment = new Comment();
		comment.setContentId(request.getContentId());
		comment.setType(request.getContentType());
		comment.setText(request.getText());

		repository.save(comment);
		return comment;
	}

	public CommentResponse getCommentById(String commentId) {
		Comment comment = repository.findById(commentId).orElseThrow(()
				-> new ResourceNotFoundException("comment", "id", commentId));
		return CommentResponse.createByComment(comment);
	}
}
