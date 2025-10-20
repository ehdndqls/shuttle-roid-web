package com.ehdndqls.shuttle;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class BasicController {

    @ControllerAdvice
    public class GlobalModelAttributes {

        @ModelAttribute
        public void addCurrentUri(HttpServletRequest request, Model model) {
            if (!model.containsAttribute("currentUri")) {
                model.addAttribute("currentUri", request.getRequestURI());
            }
        }
    }


    @GetMapping("/")
    String start(Authentication auth) {
        if(auth != null && auth.isAuthenticated())
            return "main.html";
        else
            return "login.html";
    }

    @GetMapping("/about")
    String about(){
        return "index.html";
    }

    @GetMapping("/test")
    String test(){
        return "route.html";
    }

    @GetMapping("/date")
    @ResponseBody
    String date(){
        return LocalDateTime.now().toString();
    }
}
