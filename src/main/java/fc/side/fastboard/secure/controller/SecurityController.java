package fc.side.fastboard.secure.controller;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SecurityController {

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }
}
