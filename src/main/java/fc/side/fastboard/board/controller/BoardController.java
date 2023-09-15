package fc.side.fastboard.board.controller;

import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.CreateBoardDTO;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  @GetMapping("/")
  public String index(Model model) {
    List<BoardDetailDTO> boards = boardService.getAllBoards();
    log.info("getBoards={}", boards);
    model.addAttribute("boards", boards);
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
    return "board/postForm";
  }

  @PostMapping("/board/addForm")
  public String addBoard(
          @ModelAttribute CreateBoardDTO boardDto
  ) {
    BoardDetailDTO boardDetail = boardService.createBoard(boardDto);
    return "redirect:/board/" + boardDetail.getId();
  }

  @GetMapping("/boards")
  public String getMyBoards(
          Model model,
          @PageableDefault(page=0, size=3, sort="id", direction = Sort.Direction.DESC)
          Pageable pageable
  ) {
    Page<BoardDetailDTO> boards = boardService.getMyBoards(pageable);
    int nowPage = boards.getPageable().getPageNumber() + 1;
    log.info("nowPage={}", nowPage);
    int startPage = Math.max(nowPage-3, 1);
    int endPage = Math.min(nowPage + 4, boards.getTotalPages());
    model.addAttribute("boards", boards);
    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    return "board/listForm";
  }
}
