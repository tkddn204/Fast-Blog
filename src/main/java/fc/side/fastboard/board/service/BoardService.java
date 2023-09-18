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

import static fc.side.fastboard.common.exception.BoardErrorCode.*;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;

  @Transactional
  public Page<BoardDetailDTO> findAllBoards(Pageable pageable) {
    return boardRepository.findAll(pageable).map(BoardDetailDTO::fromEntity);
  }

  @Transactional
  public Page<BoardDetailDTO> findMyBoards(Pageable pageable) {
    return boardRepository.findAll(pageable).map(BoardDetailDTO::fromEntity);
  }

  @Transactional
  public BoardDetailDTO findBoardById(int id) {
    return BoardDetailDTO.fromEntity(getBoardById(id));
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
    Board foundBoard = getBoardById(id);
    foundBoard.setTitle(boardDto.getTitle());
    foundBoard.setContent(boardDto.getContent());
  }

  @Transactional
  public void deleteBoard(int id) {
    boardRepository.findById(id)
        .ifPresentOrElse(foundBoard -> {
          boardRepository.deleteById(foundBoard.getId());
        }, () -> {
          throw new BoardException(CANNOT_DELETE_BOARD);
        });
  }

  public Board getBoardById(int id) {
    return boardRepository.findById(id)
        .orElseThrow(()->new BoardException(BOARD_NO_EXIST));
  }
}