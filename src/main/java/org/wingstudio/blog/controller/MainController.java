package org.wingstudio.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wingstudio.blog.common.Const;
import org.wingstudio.blog.po.Authority;
import org.wingstudio.blog.po.User;
import org.wingstudio.blog.service.AuthorityService;
import org.wingstudio.blog.service.UserService;
import org.wingstudio.blog.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${file.server.uploadPath}")
    private String uploadPath;

    @Value("${file.server.downloadUrl}")
    private String downloadUrl;

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "redirect:/blogs";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登陆失败");
        return "login";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user) {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(Const.ROLES.ROLE_USER_AUTHORITY_ID));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthorities(authorities);
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/search")
    public String search() {
        return "search";
    }

    @PostMapping("/uploadImg")
    @ResponseBody
    public String upload(MultipartFile file) {
        if (file == null) {
            return "";
        }
        String originalFilename = file.getOriginalFilename();
        String extendName = originalFilename.substring(originalFilename.lastIndexOf("."));
        return convert(file, extendName);
    }

    private String convert(MultipartFile file, String extendName) {
        String newFileName = UUID.randomUUID().toString() + extendName;
        File path = new File(uploadPath);
        if (!path.exists()) {
            path.setWritable(true);
            path.mkdirs();
        }
        File dest = new File(path, newFileName);
        try {
            file.transferTo(dest);
            return downloadUrl + newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @PostMapping("/uploadImgBase64")
    @ResponseBody
    public String uploadBase(MultipartFile file) {
        if (file == null)
            return null;
        String contentType = file.getContentType();
        String extend;
        switch (contentType){
            case "image/gif":extend=".gif";break;
            case "image/png":extend=".png";break;
            default:extend=".jpg";
        }
        return convert(file,extend);
    }

    @GetMapping("/403")
    @ResponseBody
    public String e403(){
        return "没有权限";
    }


}
