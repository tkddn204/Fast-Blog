package fc.side.fastboard.board.service;

import fc.side.fastboard.board.dto.BoardDetailDto;
import fc.side.fastboard.board.dto.CreateBoard;
import fc.side.fastboard.board.dto.EditBoard;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public CreateBoard.Response createBoard(CreateBoard.Request request) {

        return CreateBoard.Response.fromEntity(
                boardRepository.save(CreateBoard.Request.toEntity(request))
        );
    }

    @Transactional
    public BoardDetailDto editBoard(int id, EditBoard.Request request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());

        return BoardDetailDto.fromEntity(board);
    }
}
