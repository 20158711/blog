package org.wingstudio.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.wingstudio.blog.common.Response;
import org.wingstudio.blog.po.Authority;
import org.wingstudio.blog.po.User;
import org.wingstudio.blog.service.UserService;
import org.wingstudio.blog.util.JPAUpdateUtil;
import org.wingstudio.blog.util.ResponseUtil;
import org.wingstudio.blog.util.StringUtil;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping
    public String list(
            @RequestParam(value="async",required=false) boolean async,
            @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
            @RequestParam(value="name",required=false,defaultValue="") String name,
            Model model){
        Pageable pageable=new PageRequest(pageIndex,pageSize);
        Page<User> page=userService.listUsersByNameLike(name,pageable);
        List<User> userList=page.getContent();
        model.addAttribute("page",page);
        model.addAttribute("userList",userList);
        model.addAttribute("u","test");
        return async?"users/list::#mainContainerRepleace":"users/list";
    }

    @GetMapping("/add")
    public String createForm(Model model){
        model.addAttribute("user",new User());
        return "users/add";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Response> create(User user,Long authorityId){
        List<Authority> authorities=new ArrayList<>();
        authorities.add(new Authority(authorityId));
        user.setAuthorities(authorities);
        User oldUser;
        if (user.getId() == null) {
            user.encoderPassword(user.getPassword());
            oldUser=user;
        }else {
            oldUser = userService.getUserById(user.getId());
            if (StringUtil.equals(user.getPassword(),oldUser.getPassword()) ||
                    encoder.matches(user.getPassword(),oldUser.getPassword()))
                user.setPassword(null);
            else
                user.encoderPassword(user.getPassword());
            JPAUpdateUtil.copyNotNullProperity(user,oldUser);
        }
        try {
            userService.save(oldUser);
        }catch (ConstraintViolationException e){
            return ResponseUtil.error(e);
        }
        return ResponseEntity.ok().body(new Response(true,"success",user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id")Long id,
                                           Model model){
        try {
            userService.remoteUser(id);
        }catch (Exception e){
            return ResponseUtil.error(e);
        }
        return ResponseUtil.success();
    }

    @GetMapping("/edit/{id}")
    public String modifyForm(@PathVariable("id")Long id,Model model){
        User user=userService.getUserById(id);
        model.addAttribute("user",user);
        return "users/edit";
    }

}
