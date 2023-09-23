package fc.side.fastboard.user.controller;

import fc.side.fastboard.user.dto.MemberDto;
import fc.side.fastboard.user.entity.User;
import fc.side.fastboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/join")
    public String newMemberPage(){
        return "user/join";
    }

    @PostMapping("/join")
    public String createMemberJoin(
            @RequestPart (value = "file",required = false) MultipartFile multipartFile,
            @ModelAttribute MemberDto.Request formData
    ) {
        MemberDto.Response response = userService.saveNewMember(multipartFile, formData);
        return "redirect:/joinSuccess";
    }

    @GetMapping("/joinSuccess")
    public String successNewMemberPage(){
        return "user/joinSuccess";
    }

    @GetMapping("/edit")
    public String editPage(Principal principal, Model model){
        User userInfo = userService.findByEmail(principal.getName());
        model.addAttribute("updateMember", userInfo);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String editMemberInfo(
            @RequestPart (value = "file",required = false) MultipartFile multipartFile,
            @ModelAttribute MemberDto.Request formData
    ) {
        MemberDto.Response response = userService.updateMemberInfo(multipartFile,formData);
        return "redirect:/editSuccess";
    }

    @GetMapping("/editSuccess")
    public String successEditMemberPage(){
        return "user/editSuccess";
    }

    @GetMapping("/delete/{email}")
    public String deleteMember(@PathVariable String email){
        userService.deleteMember(email);
        return "user/deleteSuccess";
    }

}




