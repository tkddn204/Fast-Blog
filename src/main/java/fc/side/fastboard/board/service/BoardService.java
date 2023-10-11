package fc.side.fastboard.board.service;

import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.CreateBoardDTO;
import fc.side.fastboard.board.dto.EditBoardDTO;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.repository.BoardRepository;
import fc.side.fastboard.common.exception.BoardException;
import fc.side.fastboard.common.file.dto.GetFileDTO;
import fc.side.fastboard.common.file.dto.SaveFileDTO;
import fc.side.fastboard.common.file.dto.UpdateFileDTO;
import fc.side.fastboard.common.file.service.FileService;
import fc.side.fastboard.user.entity.User;
import fc.side.fastboard.user.repository.UserRepository;
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
  private final UserRepository userRepository;
  private final FileService fileService;

  @Transactional
  public Page<BoardDetailDTO> findAllBoards(Pageable pageable) {
    return boardRepository.findAll(pageable).map(BoardDetailDTO::fromEntity);
  }

  @Transactional
  public Page<BoardDetailDTO> findMyBoards(Pageable pageable) {
    return boardRepository.findAll(pageable).map(BoardDetailDTO::fromEntity);
  }

  @Transactional
  public BoardDetailDTO findBoardById(long id) {
    Board board = getBoardById(id);
    if (board.getFileId() != null) {
      GetFileDTO.Response response = fileService.getFile(
          GetFileDTO.Request.builder().query(board.getFileId().toString()).build()
      );
      return BoardDetailDTO.fromEntity(board, response.getFileId().toString());
    } else {
      return BoardDetailDTO.fromEntity(board);
    }
  }

  @Transactional
  public BoardDetailDTO createBoard(String email, CreateBoardDTO boardDto) {
    if (boardDto.getFile() == null || boardDto.getFile().isEmpty()) {
      Board newBoard = Optional.of(boardDto)
              .map(dto -> CreateBoardDTO.toEntity(dto, getUserByEmail(email)))
              .map(boardRepository::save)
              .orElseThrow(() -> new BoardException(CANNOT_SAVE_BOARD));
      return BoardDetailDTO.fromEntity(newBoard);

    } else {
      SaveFileDTO.Response response = fileService.saveFile(SaveFileDTO.Request.builder()
          .originFileName(boardDto.getFile().getOriginalFilename())
          .multipartFile(boardDto.getFile())
          .build()
      );
      Board newBoard = Optional.of(boardDto)
          .map(dto -> CreateBoardDTO.toEntity(dto, response.getFileId(), getUserByEmail(email)))
          .map(boardRepository::save)
          .orElseThrow(() -> new BoardException(CANNOT_SAVE_BOARD));
      return BoardDetailDTO.fromEntity(newBoard, response.getOriginalFileName());
    }
  }

  @Transactional
  public void editBoard(long id, EditBoardDTO boardDto) {
    Board foundBoard = getBoardById(id);
    foundBoard.setTitle(boardDto.getTitle());
    foundBoard.setContent(boardDto.getContent());
    if (boardDto.getFile() != null && boardDto.getFile().isEmpty()) {
      GetFileDTO.Response fileResponse = fileService.getFile(
          GetFileDTO.Request.builder().query(boardDto.getFile().getOriginalFilename()).build()
      );
      fileService.updateFile(UpdateFileDTO.Request.builder()
          .fileId(fileResponse.getFileId())
          .originFileName(boardDto.getFile().getOriginalFilename())
          .multipartFile(boardDto.getFile())
          .build()
      );
    }
  }

  @Transactional
  public void deleteBoard(long id) {
    boardRepository.findById(id)
        .ifPresentOrElse(foundBoard -> {
          boardRepository.deleteById(foundBoard.getId());
        }, () -> {
          throw new BoardException(CANNOT_DELETE_BOARD);
        });
  }

  public Board getBoardById(long id) {
    return boardRepository.findById(id)
        .orElseThrow(()->new BoardException(BOARD_NO_EXIST));
  }

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
            .orElseThrow(RuntimeException::new);
  }
}