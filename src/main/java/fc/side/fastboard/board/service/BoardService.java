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
        .orElseThrow(() -> new RuntimeException("새 게시글 생성 중에 에러가 발생했습니다."));

    return Optional.of(newBoard)
        .map(BoardDetailDTO::fromEntity)
        .orElseThrow(() -> new RuntimeException("DTO 변환 중에 에러가 발생했습니다."));
  }

  @Transactional
  public void editBoard(int id, EditBoardDTO boardDto) {
    Optional.of(findBoardById(id))
        .ifPresentOrElse(foundBoard -> {
          foundBoard.setTitle(boardDto.getTitle());
          foundBoard.setContent(boardDto.getContent());
        }, () -> {
          throw new RuntimeException("게시글 " + id + "번 수정 중에 에러가 발생했습니다.");
        });
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
        .orElseThrow(RuntimeException::new);    //예외처리 수정 예정
  }
}