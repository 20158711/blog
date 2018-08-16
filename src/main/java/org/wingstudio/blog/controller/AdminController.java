package org.wingstudio.blog.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wingstudio.blog.vo.Menu;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admins")
public class AdminController {

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String listUsers(Model model){
        List<Menu> menus=new ArrayList<>();
        menus.add(new Menu("用户管理","/users"));
        model.addAttribute("list",menus);
        return "admins/index";
    }
}
