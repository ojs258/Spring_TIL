package testproject.plus.Controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import testproject.plus.domain.LoginMember;
import testproject.plus.domain.Member;
import testproject.plus.repository.Service.ServiceRepository;

import java.util.HashMap;

@Controller
public class MemberController {
    ServiceRepository serviceRepository = new ServiceRepository();

    @GetMapping("/joinMember")
    public String JoinMember(){
        return "member/memberForm";
    }
    @PostMapping("/joinMember")
    public String Join(Member member){
        serviceRepository.joinMember(member);
        return "redirect:/";
    }
    @PostMapping("/listMember")
    public String Delete(String id){
        serviceRepository.delMember(id);
        return "redirect:/listMember";
    }
    @GetMapping("/listMember")
    public String List(Model model){
        model.addAttribute("members",serviceRepository.listMember());
        return "/member/listMember";
    }
    @GetMapping("/loginMember")
    public String Login(LoginMember member){
        HashMap<String, String> resultMember = serviceRepository.loginMember(member);
        if (resultMember.get(member.getId()).equals("로그인 성공")) {
            return "member/loginHome";
        }else {
            return "redirect:/";
        }
    }
}
