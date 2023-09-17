package fc.side.fastboard.board.service;

import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.CreateBoardDTO;
import fc.side.fastboard.board.dto.EditBoardDTO;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.repository.BoardRepository;
import fc.side.fastboard.common.exception.BoardException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static fc.side.fastboard.common.exception.BoardErrorCode.BOARD_NO_EXIST;
import static fc.side.fastboard.common.exception.BoardErrorCode.CANNOT_SAVE_BOARD;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;

  @Transactional
  public Page<BoardDetailDTO> getAllBoards(Pageable pageable) {
    return boardRepository.findAll(pageable).map(BoardDetailDTO::fromEntity);
  }

  @Transactional
  public Page<BoardDetailDTO> getMyBoards(Pageable pageable) {
    return boardRepository.findAll(pageable).map(BoardDetailDTO::fromEntity);
  }

  @Transactional
  public BoardDetailDTO getBoardById(int id) {
    return BoardDetailDTO.fromEntity(findBoardById(id));
  }

  @Transactional
  public BoardDetailDTO createBoard(CreateBoardDTO boardDto) {
    Board newBoard = Optional.of(boardDto)
        .map(CreateBoardDTO::toEntity)
        .map(boardRepository::save)
        .orElseThrow(() -> new BoardException(CANNOT_SAVE_BOARD));

    return BoardDetailDTO.fromEntity(newBoard);
  }

  @Transactional
  public void editBoard(int id, EditBoardDTO boardDto) {
    Board foundBoard = findBoardById(id);
    foundBoard.setTitle(boardDto.getTitle());
    foundBoard.setContent(boardDto.getContent());
  }

  @Transactional
  public void deleteBoard(int id) {
    Optional.of(findBoardById(id))
        .ifPresentOrElse(foundBoard -> {
          boardRepository.deleteById(foundBoard.getId());
        }, () -> {
          throw new RuntimeException("게시글 " + id + "번을 삭제하는데 실패했습니다.");
        });
  }

  public Board findBoardById(int id) {
    return boardRepository.findById(id)
        .orElseThrow(()->new BoardException(BOARD_NO_EXIST));
  }
}