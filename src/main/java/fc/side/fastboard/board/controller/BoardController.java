package fc.side.fastboard.board.controller;

import fc.side.fastboard.board.dto.BoardDetailDTO;
import fc.side.fastboard.board.dto.CreateBoardDTO;
import fc.side.fastboard.board.dto.EditBoardDTO;
import fc.side.fastboard.board.entity.Board;
import fc.side.fastboard.board.service.BoardService;
import fc.side.fastboard.board.util.PageNumber;
import fc.side.fastboard.common.file.dto.GetFileDTO;
import fc.side.fastboard.common.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;
  private final FileService fileService;

  @GetMapping("/")
  public String index(
          Model model,
          @PageableDefault(size=6, sort="id", direction = Sort.Direction.DESC)
          Pageable pageable
  ) {
    Page<BoardDetailDTO> boards = boardService.findAllBoards(pageable);
    PageNumber<BoardDetailDTO> pageNumber = new PageNumber<>(boards);

    model.addAttribute("boards", boards);
    model.addAttribute("pageNumber", pageNumber);
    return "index-temp";
  }

  @GetMapping("/board/{boardId}")
  public String getBoard(
      @PathVariable Long boardId,
      Model model
  ) {
    BoardDetailDTO findBoard = boardService.findBoardById(boardId);
    model.addAttribute("board", findBoard);
    return "board/detailForm";
  }

  @GetMapping("/board/addForm")
  public String addForm(@ModelAttribute("board") CreateBoardDTO createBoardDTO) {
    return "board/postForm";
  }

  @PostMapping("/board/addForm")
  public String addBoard(
      @Validated
      @ModelAttribute("board") CreateBoardDTO boardDto,
      BindingResult bindingResult,  //ModelAttribute 뒤에 써야됩니다.
      RedirectAttributes redirectAttributes,
      Principal principal
  ) {
    if(bindingResult.hasErrors()) {
      log.info("validation-errors={}", bindingResult);
      return "board/postForm";
    }

    BoardDetailDTO boardDetail = boardService.createBoard(principal.getName(), boardDto);
    redirectAttributes.addAttribute("id", boardDetail.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/board/" + boardDetail.getId();
  }

  @GetMapping("/board/editForm/{boardId}")
  public String editForm(
      @PathVariable Long boardId,
      Model model
  ) {
    Board findBoard = boardService.getBoardById(boardId);
    GetFileDTO.Response response = fileService.getFile(
        GetFileDTO.Request.builder().query(findBoard.getFileId().toString()).build()
    );
    model.addAttribute("board", findBoard);
    model.addAttribute("fileId", response.getFileId().toString());
    model.addAttribute("fileName", response.getOriginFileName());
    return "board/editForm";
  }

  @PostMapping("/board/editForm/{boardId}")
  public String editBoard(
      @PathVariable Long boardId,
      @Validated @ModelAttribute("board") EditBoardDTO boardDto,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ) {
    if(bindingResult.hasErrors()) {
      log.info("validation-errors={}", bindingResult);
      return "board/editForm";
    }

    boardService.editBoard(boardId, boardDto);
    redirectAttributes.addAttribute("status", true);

    return "redirect:/board/" + boardId;
  }

  @GetMapping("/board/deleteForm/{boardId}")
  public String deleteForm(
      @PathVariable Long boardId
  ) {
    boardService.deleteBoard(boardId);
    return "redirect:/";
  }


  @GetMapping("/boards")
  public String getMyBoards(
      Model model,
      @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC)
      Pageable pageable
  ) {
    Page<BoardDetailDTO> boards = boardService.findMyBoards(pageable);
    PageNumber<BoardDetailDTO> pageNumber = new PageNumber<>(boards);

    model.addAttribute("boards", boards);
    model.addAttribute("pageNumber", pageNumber);
    return "board/listForm";
  }

}
