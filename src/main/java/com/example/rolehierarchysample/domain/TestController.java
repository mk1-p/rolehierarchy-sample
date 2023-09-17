package com.example.rolehierarchysample.domain;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/admin")
    public String getAdminMessage() {
        return "Hi Admin!";
    }

    @GetMapping("/user")
    public String getUserMessage() {
        return "Hi User!";
    }

    @GetMapping("/guest")
    public String getGuestMessage() {
        return "Hi Guest!";
    }



    @PreAuthorize("hasAuthority('ROLE_API')")
    @GetMapping("/api")
    public String getApiMessage() {
        return "Hi Api!";
    }

}
