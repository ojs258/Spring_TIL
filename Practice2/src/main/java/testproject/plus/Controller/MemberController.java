package testproject.plus.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import testproject.plus.domain.LoginMember;
import testproject.plus.domain.Member;
import testproject.plus.repository.Service.ServiceRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class MemberController {
    ServiceRepository serviceRepository = new ServiceRepository();

    @GetMapping("/joinMember")
    public String JoinMember(){
        return "member/createMemberForm";
    }
    @PostMapping("/joinMember")
    public String Join(Member member){
        serviceRepository.joinMember(member);
        return "redirect:/";
    }
    @GetMapping("/listMember")
    public String List(Model model){
        List<Member> members = serviceRepository.listMember();
        model.addAttribute("members",members);
        return "member/listMember";
    }
    @PostMapping("/listMember")
    public String Delete(String id){
        serviceRepository.delMember(id);
        return "redirect:member/listMember";
    }
    @GetMapping("/loginMember")
    public String loginMember(){
        return "member/loginMemberForm";
    }
    @PostMapping ("/loginMember")
    public String Login(LoginMember lmember){
        Optional<Member> member = serviceRepository.loginMember(lmember);
        if (Boolean.TRUE) {
            return "member/loginHome";
        }else{
            return "member/loginFailed";
        }
    }
}
