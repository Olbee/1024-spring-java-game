package gamestudio.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class authenticationController {

    @RequestMapping("/register")
    public String getRegisterPage() {
        return "register_page_1024";
    }

    @RequestMapping("/login")
    public String getLoginPage() {
        return "login_page_1024";
    }



}
