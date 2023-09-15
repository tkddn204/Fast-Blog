package fc.side.fastboard.board.service;

import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.CreateBoardDTO;
import fc.side.fastboard.board.dto.EditBoardDTO;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
  public Page<BoardDetailDTO> getMyBoards(Pageable pageable) {
    return boardRepository.findAll(pageable).map(BoardDetailDTO::fromEntity);
  }

  @Transactional
  public BoardDetailDTO getBoardById(int id) {
    return BoardDetailDTO.fromEntity(findBoardById(id));
  }

  @Transactional
  public BoardDetailDTO createBoard(CreateBoardDTO boardDto) {

    return BoardDetailDTO.fromEntity(
        boardRepository.save(CreateBoardDTO.toEntity(boardDto))
    );
  }

  @Transactional
  public BoardDetailDTO editBoard(int id, EditBoardDTO boardDto) {
    Board board = findBoardById(id);
    board.setTitle(boardDto.getTitle());
    board.setContent(boardDto.getContent());

    return BoardDetailDTO.fromEntity(board);
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