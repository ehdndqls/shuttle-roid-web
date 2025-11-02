package com.ehdndqls.shuttle;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object messageObj = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        int statusCode = 500; // 기본값
        if (statusObj != null) {
            statusCode = Integer.parseInt(statusObj.toString());
        }

        String errorMsg = "오류가 발생했습니다";
        String detailMsg = "요청을 처리하는 중 문제가 발생했습니다.";

        if (statusCode == 404) {
            errorMsg = "페이지를 찾을 수 없습니다";
            detailMsg = "요청하신 페이지가 존재하지 않습니다.";
        } else if (statusCode == 403) {
            errorMsg = "접근이 금지되었습니다";
            detailMsg = "권한이 없는 페이지에 접근하였습니다.";
        } else if (statusObj != null && messageObj != null) {
            detailMsg = messageObj.toString();
        }

        model.addAttribute("status", statusCode);
        model.addAttribute("error", errorMsg);
        model.addAttribute("message", detailMsg);

        return "error"; // templates/error.html
    }
}
