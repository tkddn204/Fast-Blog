package fc.side.fastboard.user.controller;

import fc.side.fastboard.user.dto.MemberDto;
import fc.side.fastboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    @GetMapping("/user/login")
    public String memberLoginPage(){
        return "user/loginForm";
    }


    @GetMapping("/user/join")
    public String newMemberPage(){
        return "user/joinForm";
    }

    @PostMapping("/user/join")
    public String createMemberJoin(MemberDto.Request formData){
        MemberDto.Response response = userService.saveNewMember(formData);
        System.out.println("=======================");
        System.out.println(response + "is saved");
        return "redirect:/";
    }

    @GetMapping("/user/edit")
    public String editPage(Model model){
        model.addAttribute("1", "memberId");
        return "user/editForm";
    }

    @PutMapping("/user/edit")
    public String editMemberInfo(
            @PathVariable Integer memberId,
            Model model
    ){
        System.out.println(memberId);

        return "index";
    }
}
