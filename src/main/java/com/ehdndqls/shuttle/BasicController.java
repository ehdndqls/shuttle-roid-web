package com.ehdndqls.shuttle;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class BasicController {

    @GetMapping("/")
    String start(Authentication auth) {
        if(auth != null && auth.isAuthenticated())
            return "main.html";
        else
            return "login.html";
    }

    @GetMapping("/about")
    @ResponseBody
    String about(){
        return "index.html";
    }

    @GetMapping("/test")
    String test(){
        return "index.html";
    }

    @GetMapping("/date")
    @ResponseBody
    String date(){
        return LocalDateTime.now().toString();
    }
}
