package fc.side.fastboard.comment.controller;

import fc.side.fastboard.comment.dto.CreateCommentDTO;
import fc.side.fastboard.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{boardId}")
    public String addComment(
            @ModelAttribute("comment") CreateCommentDTO commentDto,
            @PathVariable Long boardId,
            RedirectAttributes redirectAttributes,
            Principal principal
    ) {
        commentService.createComment(principal.getName(), commentDto, boardId);

        redirectAttributes.addAttribute("boardId", boardId);
        return "redirect:/board/{boardId}";

    }
}
