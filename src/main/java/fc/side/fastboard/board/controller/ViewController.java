package fc.side.fastboard.board.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ViewController {
  @GetMapping("")
  public String index() {
    return "index";
  }

  @GetMapping("/board")
  public String board() {
    return "board";
  }

}
