package com.aradata.plaidapp.service.comment;

import com.aradata.plaidapp.exception.ResourceNotFoundException;
import com.aradata.plaidapp.model.comments.Comment;
import com.aradata.plaidapp.model.content.request.CommentRequest;
import com.aradata.plaidapp.repository.CommentRepository;
import com.aradata.plaidapp.security.UserPrincipal;
import com.aradata.plaidapp.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

	@Autowired
	private CommentRepository repository;

	@Autowired
	private UsersService usersService;

	public Page<Comment> findAllByContentId(Pageable pageable, String contentId) {
		return repository.findAllByContentIdAndIsReply(pageable, contentId, false);
//		return repository.findAllByContentId(pageable, contentId);
	}

	public Comment createComment(UserPrincipal currentUser, CommentRequest request, String contentId) {
		Comment comment = new Comment();
		comment.setOwnerId(currentUser.getId());
		comment.setContentId(contentId);
		comment.setText(request.getText().trim());

		comment.setOwnerName(usersService.findById(currentUser.getId()).getName());
		repository.save(comment);
		return comment;
	}

	public Comment reply(UserPrincipal currentUser, CommentRequest request, String contentId, String commentId) {
		Comment reply = createComment(currentUser, request, contentId);
		reply.setReply(true);
		reply.setReplyTo(commentId);
		Comment save = repository.save(reply);
		Comment comment = repository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment", "id", commentId));
		boolean isReply = comment.isReply();
		comment.addReply(save);
		if (!isReply) {
			repository.save(comment);
		} else {
			while(isReply) {
				Comment replyTo = repository.findById(comment.getReplyTo()).get();
				Comment finalComment = comment;
				replyTo.getReplies().stream()
						.filter(comment1 -> comment1.getId().equals(finalComment.getId()))
						.findFirst()
						.get().setReplies(comment.getReplies());
				comment = replyTo;
				isReply = comment.isReply();
			}
		}
		repository.save(comment);
		return save;
	}

}
