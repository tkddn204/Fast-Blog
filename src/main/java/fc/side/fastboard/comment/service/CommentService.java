package fc.side.fastboard.comment.service;

import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.repository.BoardRepository;
import fc.side.fastboard.comment.dto.CreateCommentDTO;
import fc.side.fastboard.comment.entity.Comment;
import fc.side.fastboard.comment.repository.CommentRepository;
import fc.side.fastboard.user.entity.User;
import fc.side.fastboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void createComment(String email, CreateCommentDTO commentDto, Long boardId) {

        User user = getUserByEmail(email);
        Board board = getBoardById(boardId);

        commentRepository.save(
                Comment.builder()
                        .user(user)
                        .content(commentDto.getContent())
                        .board(board)
                        .build()
        );
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(RuntimeException::new);
    }

    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(RuntimeException::new);
    }
}
