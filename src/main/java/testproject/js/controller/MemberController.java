package testproject.js.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import testproject.js.domain.Member;
import testproject.js.service.MemberService;

import java.util.List;

@Controller
public class MemberController {
    private final MemberService memberService;
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/sign-up")
    public String signUpFrom(){
        return "member/createMember";
    }
    @PostMapping("/member/sign-up")
    public String signUp(MemberForm form){
        Member member = new Member();
        member.setId(form.getId());
        member.setName(form.getName());
        member.setPw(form.getPw());

        memberService.join(member);

        return "redirect:/";
    }
    @GetMapping("/member/list")
    public String list(Model model){
        List<Member> members = memberService.memberList();
        model.addAttribute("members",members);
        return "member/memberList";
    }
}
