package com.isep.certification.config.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/api/error")
public class ApplicationErrorController {
    @GetMapping("")
    public String handleError(HttpServletRequest request, Model model) {
        // Get the error status code
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            // Handle 403 Forbidden specifically
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorTitle", "Access Denied");
                model.addAttribute("errorMessage", "You do not have permission to access this resource.");
                return "error";
            }
        }
        
        // Default error page for other errors
        return "error";
    }
}
