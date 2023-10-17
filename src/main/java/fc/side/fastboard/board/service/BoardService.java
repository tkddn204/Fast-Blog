package fc.side.fastboard.board.service;

import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.CreateBoardDTO;
import fc.side.fastboard.board.dto.EditBoardDTO;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.repository.BoardRepository;
import fc.side.fastboard.common.exception.BoardException;
import fc.side.fastboard.common.file.dto.*;
import fc.side.fastboard.common.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static fc.side.fastboard.common.exception.BoardErrorCode.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileService fileService;

    @Transactional
    public Page<BoardDetailDTO> findAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable).map(BoardDetailDTO::fromEntity);
    }

    @Transactional
    public Page<BoardDetailDTO> findMyBoards(Pageable pageable) {
        return boardRepository.findAll(pageable).map(BoardDetailDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public BoardDetailDTO findBoardById(int id) {
        Board board = getBoardById(id);
        if (board.getStoredFileName() != null) {
            FileResponseDTO response = fileService.getFile(board.getStoredFileName());
            return BoardDetailDTO.fromEntity(board, response.originFileName(), response.storedFileName());
        } else {
            return BoardDetailDTO.fromEntity(board);
        }
    }

    @Transactional
    public BoardDetailDTO createBoard(CreateBoardDTO boardDto) {
        if (boardDto.getFile() == null || boardDto.getFile().isEmpty()) {
            Board newBoard = Optional.of(boardDto)
                    .map(CreateBoardDTO::toEntity)
                    .map(boardRepository::save)
                    .orElseThrow(() -> new BoardException(CANNOT_SAVE_BOARD));

            return BoardDetailDTO.fromEntity(newBoard);
        } else {
            FileResponseDTO fileResponse = fileService.saveFile(boardDto.getFile());
            Board newBoard = Optional.of(boardDto)
                    .map(dto -> CreateBoardDTO.toEntity(dto, fileResponse.storedFileName()))
                    .map(boardRepository::save)
                    .orElseThrow(() -> new BoardException(CANNOT_SAVE_BOARD));
            return BoardDetailDTO.fromEntity(newBoard, fileResponse.originFileName(), fileResponse.storedFileName());
        }
    }

    @Transactional
    public void editBoard(int id, EditBoardDTO boardDto) {
        Board foundBoard = getBoardById(id);
        foundBoard.setTitle(boardDto.getTitle());
        foundBoard.setContent(boardDto.getContent());

        // 이미 보드에 이미지가 있었을 때
        if (foundBoard.getStoredFileName() != null) {
            // 이미지를 수정하지 않았을 경우
            if (foundBoard.getStoredFileName().equals(boardDto.getFileName())) return;
                // 이미지를 삭제했을 경우
            else if (boardDto.getFileName() == null || boardDto.getFileName().isBlank()) {
                deleteImageFile(foundBoard);
                return;
            }
        }

        // 업로드된 이미지로 수정
        if (boardDto.getFileName() != null) {
            updateImageFile(boardDto.getFile(), foundBoard);
        }
    }

    private void deleteImageFile(Board foundBoard) {
        fileService.deleteFile(
                DeleteFileDTO.Request.builder()
                        .storedFileName(foundBoard.getStoredFileName())
                        .build()
        );
        foundBoard.setStoredFileName(null);
    }

    private void updateImageFile(MultipartFile multipartFile, Board foundBoard) {
        FileResponseDTO updateFileResponse = fileService.updateFile(
                foundBoard.getStoredFileName(),
                multipartFile
        );
        foundBoard.setStoredFileName(updateFileResponse.storedFileName());
    }

    @Transactional
    public void deleteBoard(int id) {
        boardRepository.findById(id)
                .ifPresentOrElse(foundBoard -> {
                    if (foundBoard.getStoredFileName() != null) {
                        DeleteFileDTO.Request fileDeleteRequest = DeleteFileDTO.Request.builder()
                                .storedFileName(foundBoard.getStoredFileName())
                                .build();
                        fileService.deleteFile(fileDeleteRequest);
                    }
                    boardRepository.deleteById(foundBoard.getId());
                }, () -> {
                    throw new BoardException(CANNOT_DELETE_BOARD);
                });
    }

    @Transactional(readOnly = true)
    public Board getBoardById(int id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(BOARD_NO_EXIST));
    }
}