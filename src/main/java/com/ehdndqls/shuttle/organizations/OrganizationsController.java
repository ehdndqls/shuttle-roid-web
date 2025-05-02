package com.ehdndqls.shuttle.organizations;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrganizationsController {

    private final OrganizationsRepository organizationsRepository;
    private final OrganizationsService organizationsService;

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @GetMapping("/join")
    public String join(){
        return "join.html";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestParam String organizationName,
                                      @RequestParam String organizationPassword,
                                      @RequestParam String confirmPassword) {
        try {
            // 비밀번호 일치 확인
            if (!organizationPassword.equals(confirmPassword)) {
                return ResponseEntity.badRequest().body(Map.of("errorMessage", "비밀번호가 일치하지 않습니다."));
            }

            // 회원가입 처리 (DB 저장 등)
            organizationsService.saveOrganization(organizationName, organizationPassword);

            return ResponseEntity.ok().build();  // 성공 시 200 OK 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("errorMessage", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("errorMessage", "등록 중 오류 발생"));
        }
    }
}
