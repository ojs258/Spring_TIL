package com.example.springpractice.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpringPracticeController {
    @GetMapping("SpringPractice")
    public String SpringPractice(Model model) {
        model.addAttribute("data","Practice!!!");
        return "SpringPractice";
    }
    @GetMapping("practice-mvc")
    public String practiceMvc(@RequestParam("name") String name, Model model){
       model.addAttribute("name", name);
       return "practice-template";
    }

    @GetMapping("practice-string")
    @ResponseBody
    public String practiceString(@RequestParam("name") String name, Model model){
        model.addAttribute("name",name);
        return "practice" + name;
    }

    @GetMapping("practice-api")
    @ResponseBody
    public Practice practiceApi(@RequestParam("name") String name) {
        Practice practice = new Practice();
        practice.setName(name);
        return practice;
    }
        static class Practice{

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

}
