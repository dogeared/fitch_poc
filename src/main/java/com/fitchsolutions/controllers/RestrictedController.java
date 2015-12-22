package com.fitchsolutions.controllers;

import com.fitchsolutions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RestrictedController extends ControllerBase {
    @RequestMapping("/restricted")
    public String secret(HttpServletRequest req, Model model) {
        if (!isAuthenticated(req)) {
            return "redirect:/login";
        }

        model.addAttribute("orgImageHref", getLogoHref(req));

        return "restricted";
    }

    @RequestMapping("/admin")
    public String admin(HttpServletRequest req) throws UnauthorizedException {
        if (!isAdmin(req)) {
            throw new UnauthorizedException();
        }

        return "admin";
    }

    @ExceptionHandler(UnauthorizedException.class)
    public void unauthorized(HttpServletResponse resp) throws Exception {
        resp.sendError(HttpStatus.UNAUTHORIZED.value(), "You are not an Admin.");
    }
}
