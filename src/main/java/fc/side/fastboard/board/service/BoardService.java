package fc.side.fastboard.board.service;

import fc.side.fastboard.board.dto.BoardDetailDto;
import fc.side.fastboard.board.dto.CreateBoard;
import fc.side.fastboard.board.dto.EditBoard;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public List<BoardDetailDto> getAllBoardDetails() {
        return boardRepository.findAll().stream()
                .map(BoardDetailDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public BoardDetailDto getBoardDetail(int id) {
        return BoardDetailDto.fromEntity(findBoardById(id));
    }

    @Transactional
    public CreateBoard.Response createBoard(CreateBoard.Request request) {

        return CreateBoard.Response.fromEntity(
                boardRepository.save(CreateBoard.Request.toEntity(request))
        );
    }

    @Transactional
    public BoardDetailDto editBoard(int id, EditBoard.Request request) {
        Board board = findBoardById(id);
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());

        return BoardDetailDto.fromEntity(board);
    }


    @Transactional
    public void deleteBoard(int id) {
        findBoardById(id);
        boardRepository.deleteById(id);     //예외처리 수정 예정

    }

    public Board findBoardById(int id) {
        return boardRepository.findById(id)
                .orElseThrow(RuntimeException::new);    //예외처리 수정 예정
    }
}