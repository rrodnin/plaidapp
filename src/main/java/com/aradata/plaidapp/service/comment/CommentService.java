package com.aradata.plaidapp.service.comment;

import com.aradata.plaidapp.model.comments.Comment;
import com.aradata.plaidapp.model.content.request.CommentRequest;
import com.aradata.plaidapp.repository.CommentRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

	@Autowired
	private CommentRepository repository;

	public Page<Comment> findAllByContentId(Pageable pageable, String contentId) {
		return repository.findAllByContentId(pageable, contentId);
	}

	public Comment createComment(UserPrincipal currentUser, CommentRequest request, String contentId) {
		Comment comment = new Comment();
		comment.setOwnerId(currentUser.getId());
		comment.setContentId(contentId);
		comment.setText(request.getText());
		repository.save(comment);
		return comment;
	}
}
