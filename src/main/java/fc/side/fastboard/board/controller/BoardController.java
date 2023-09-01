package fc.side.fastboard.board.controller;

import fc.side.fastboard.board.dto.BoardDTO;
import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.request.CreateBoardRequest;
import fc.side.fastboard.board.dto.request.UpdateBoardRequest;
import fc.side.fastboard.board.service.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

  private final BoardServiceImpl boardService;

  @GetMapping("/boards")
  public List<BoardDTO> getBoards() {
    log.info("GET /boards HTTP/1.1");

    // TODO: 페이지네이션에 대한 의논 필요
    return boardService.getBoardList(0);
  }

  @GetMapping("/board/{id}")
  public BoardDetailDTO getBoardDetail(
      @PathVariable final int id
  ) {
    log.info("GET /board/{} HTTP/1.1", id);

    return boardService.getBoardById(id);
  }

  @PostMapping("/board")
  public int createBoard(
      @RequestBody final CreateBoardRequest request
  ) {
    log.info("POST /board HTTP/1.1");
    log.info("request : {}", request.getTitle());

    // TODO: 생성 후 동작(어디로 리다이렉트할지)에 대한 의논 필요
    return boardService.createBoard(request);
  }

  @PutMapping("/board/{id}")
  public int updateBoard(
      @PathVariable final int id,
      @RequestBody final UpdateBoardRequest request
  ) {
    log.info("PUT /board/{} HTTP/1.1", id);

    // TODO: 수정 후 동작(어디로 리다이렉트할지)에 대한 의논 필요
    return boardService.updateBoard(request);
  }

  @DeleteMapping("/board/{id}")
  public int deleteBoard(
      @PathVariable final int id
  ) {
    log.info("DELETE /board/{} HTTP/1.1", id);

    // TODO: 삭제 후 동작(어디로 리다이렉트할지)에 대한 의논 필요
    return boardService.deleteBoard(id);
  }
}
