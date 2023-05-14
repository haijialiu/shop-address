package com.hziee.shop_address.controller;

import com.hziee.shop_address.entity.User;
import com.hziee.shop_address.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin(){
        return "login";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }
    @GetMapping("/signup")
    public String showSignup(){
        return "signup";
    }
    @PostMapping("/signup")
    public String signup(User user, Model model){
        try {
            userService.save(user);
        }catch (DuplicateKeyException e){
            model.addAttribute("error",true);
            return "signup";
        }
        return "login";
    }
}
