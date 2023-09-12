package fc.side.fastboard.board.controller.api;

import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.CreateBoard;
import fc.side.fastboard.board.dto.EditBoard;
import fc.side.fastboard.board.dto.ResponseDto;
import fc.side.fastboard.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardApiController {

  private final BoardService boardService;

  @GetMapping("/boards")
  public List<BoardDetailDTO> getBoards() {
    log.info("GET /api/boards HTTP/1.1");

    return boardService.getAllBoards();

    // TODO: 페이지네이션에 대한 의논 필요
  }

  @GetMapping("/board/{id}")
  public BoardDetailDTO getBoardDetail(
      @PathVariable final int id
  ) {
    log.info("GET /api/board/{} HTTP/1.1", id);

    return boardService.getBoardById(id);
  }

  @PostMapping("/board")
  public CreateBoard.Response createBoard(
      @RequestBody final CreateBoard.Request request
  ) {
    log.info("POST /api/board HTTP/1.1");
    log.info("request : {}", request);

    return boardService.createBoard(request);

    // TODO: 생성 후 동작(어디로 리다이렉트할지)에 대한 의논 필요
  }

  @PutMapping("/board/{id}")
  public BoardDetailDTO updateBoard(
          @PathVariable final int id,
          @RequestBody final EditBoard.Request request
          ) {
    log.info("PUT /api/board/{} HTTP/1.1", id);

    return boardService.editBoard(id, request);

    // TODO: 수정 후 동작(어디로 리다이렉트할지)에 대한 의논 필요
  }

  @DeleteMapping("/board/{id}")
  public ResponseDto<Integer> deleteBoard(
      @PathVariable final int id
  ) {
    log.info("DELETE /api/board/{} HTTP/1.1", id);

    boardService.deleteBoard(id);

    return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);

    // TODO: 삭제 후 동작(어디로 리다이렉트할지)에 대한 의논 필요
  }
}
