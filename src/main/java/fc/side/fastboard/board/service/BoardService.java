package fc.side.fastboard.board.service;

import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.CreateBoard;
import fc.side.fastboard.board.dto.EditBoard;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;

  @Transactional
  public List<BoardDetailDTO> getAllBoards() {
    return boardRepository.findAll().stream()
        .map(BoardDetailDTO::fromEntity).collect(Collectors.toList());
  }

  @Transactional
  public BoardDetailDTO getBoardById(int id) {
    return BoardDetailDTO.fromEntity(findBoardById(id));
  }

  @Transactional
  public CreateBoard.Response createBoard(CreateBoard.Request request) {
    Board newBoard = Optional.of(request).map(CreateBoard.Request::toEntity)
        .map(boardRepository::save)
        .orElseThrow(() -> new RuntimeException("새 게시글 생성 중에 에러가 발생했습니다."));

    return Optional.of(newBoard)
        .map(CreateBoard.Response::fromEntity)
        .orElseThrow(() -> new RuntimeException("DTO 변환 중에 에러가 발생했습니다."));
  }

  @Transactional
  public void editBoard(int id, EditBoard.Request request) {
    Optional.of(findBoardById(id))
        .ifPresentOrElse(foundBoard -> {
          foundBoard.setTitle(request.getTitle());
          foundBoard.setContent(request.getContent());
        }, () -> {
          throw new RuntimeException("게시글 수정 중에 에러가 발생했습니다.");
        });
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