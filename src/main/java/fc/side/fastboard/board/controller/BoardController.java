package fc.side.fastboard.board.controller;

import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.CreateBoard;
import fc.side.fastboard.board.dto.EditBoard;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  @GetMapping("")
  public String index() {
    return "index";
  }

  @GetMapping("/board/{boardId}")
  public String board(
      @PathVariable Integer boardId,
      Model model
  ) {
    Board findBoard = boardService.findBoardById(boardId);
    model.addAttribute("board", findBoard);
    return "board/detailForm";
  }

  @GetMapping("/board/addForm")
  public String addForm() {
    return "board/editForm";
  }

  @PostMapping("/board/addForm")
  public String addBoard(
      @ModelAttribute @Valid CreateBoard.Request boardRequest
  ) {
    CreateBoard.Response boardResponse = boardService.createBoard(boardRequest);
    return "redirect:/board/" + boardResponse.getId();
  }

  @GetMapping("/board/editForm/{boardId}")
  public String editForm(
      @PathVariable Integer boardId,
      Model model
  ) {
    Board findBoard = boardService.findBoardById(boardId);
    model.addAttribute("board", findBoard);
    return "board/editForm";
  }

  @PostMapping("/board/editForm/{boardId}")
  public String editBoard(
      @PathVariable Integer boardId,
      @ModelAttribute @Valid EditBoard.Request boardRequest
  ) {
    boardService.editBoard(boardId, boardRequest);
    return "redirect:/board/" + boardId;
  }

  @GetMapping("/boards")
  public String test(Model model) {
    List<BoardDetailDTO> boards = boardService.getAllBoards();
    model.addAttribute("boards", boards);
    return "board/listForm";
  }
}
