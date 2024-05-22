package com.admin.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Error Controller
 */
@Controller
public class ErrorController {

    /**
     * 에러 페이지
     *
     * @return error.html
     */
    @GetMapping("/error")
    public String getErrorPage(){
        return "error";
    }
}
