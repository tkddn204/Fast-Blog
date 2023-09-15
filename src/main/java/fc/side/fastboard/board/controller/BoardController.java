package fc.side.fastboard.board.controller;

import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.CreateBoardDTO;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;
  @Value("${pagination.width}")
  private int width;

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
          @PageableDefault(size=3, sort="id", direction = Sort.Direction.DESC)
          Pageable pageable
  ) {
    Page<BoardDetailDTO> boards = boardService.getMyBoards(pageable);
    model.addAttribute("boards", boards);
    setPagingModel(model, pageable.getPageNumber(), boards.getTotalPages());
    return "board/listForm";
  }

  private void setPagingModel(Model model, int pageNum, int totalPages) {
    log.info("width={}", width);

    int nowPage = pageNum + 1;
    int startPage = Math.max(nowPage-3, 1);
    int endPage = Math.min(nowPage + 4, totalPages);
    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
  }


}
