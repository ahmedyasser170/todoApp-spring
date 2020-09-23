package com.in28minutes.springboot.web.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

// /login => helloworld
@Controller
@SessionAttributes("name")
public class WelcomeController {


    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String showWelcomePage(ModelMap modelMap) {
        modelMap.put("name",getLoggedInUserName());
        return "welcome";
    }

    private String getLoggedInUserName() {
        Object principal =
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

}
