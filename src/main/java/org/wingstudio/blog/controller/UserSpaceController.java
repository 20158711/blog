package org.wingstudio.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.wingstudio.blog.common.Const;
import org.wingstudio.blog.common.Response;
import org.wingstudio.blog.po.Blog;
import org.wingstudio.blog.po.Catalog;
import org.wingstudio.blog.po.User;
import org.wingstudio.blog.po.Vote;
import org.wingstudio.blog.service.BlogService;
import org.wingstudio.blog.service.CatalogService;
import org.wingstudio.blog.service.UserService;
import org.wingstudio.blog.util.JPAUpdateUtil;
import org.wingstudio.blog.util.ResponseUtil;
import org.wingstudio.blog.util.SecurityUtil;
import org.wingstudio.blog.util.StringUtil;

import java.util.List;

@Controller
@RequestMapping("/u")
public class UserSpaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;

    @Value("${file.server.uploadUrl}")
    private String fileServerUrl;

    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username, Model model) {
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "redirect:/u/" + username + "/blogs";
    }

    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String profile(@PathVariable("username") String username, Model model) {
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("fileServerUrl",fileServerUrl);
        return "userspace/profile";
    }

    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username, User user) {
        User oldUser = userService.getUserById(user.getId());

        if (StringUtil.equals(oldUser.getPassword(),user.getPassword()) || passwordEncoder.matches(oldUser.getPassword(), user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        JPAUpdateUtil.copyNotNullProperity(user, oldUser);
        userService.save(oldUser);

        return "redirect:/u/" + username + "/profile";
    }

    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public String avatar(@PathVariable("username") String username, Model model) {
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "userspace/avatar";
    }

    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(
            @PathVariable("username") String username,
            @RequestBody User user) {
        String avatar = user.getAvatar();
        User userById = userService.getUserById(user.getId());
        userById.setAvatar(avatar);
        userService.save(userById);
        return ResponseUtil.success(avatar);
    }

    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(
            @PathVariable("username") String username,
            @RequestParam(value = "order", required = false, defaultValue = "new") String order,
            @RequestParam(value = "catalog", required = false) Long catalogId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "async", required = false) boolean async,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            Model model) {
        User user = userService.getUserByUsername(username);

        Page<Blog> page=blogService.listBlogsByOrder(user,order,catalogId,keyword,pageIndex,pageSize);

        List<Blog> list = page.getContent();

        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        return async ? "userspace/u::#mainContainerRepleace" : "userspace/u";
    }

    @GetMapping("/{username}/blogs/{id}")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @PathVariable("id") Long id,Model model) {
        Blog blog = blogService.getBlogById(id);
        //阅读量加1
        blogService.readingIncrease(id);

        //判断是否是博客所有者,所有者才有编辑按键
        boolean isBlogOwner = SecurityUtil.isOwner(username);
        User principal=SecurityUtil.getUser();

        //判断点赞情况
        List<Vote> votes = blog.getVotes();
        Vote currentVote = null;
        if (principal != null) {
            for (Vote vote : votes) {
                if (vote.getUser().getUsername().equals(principal.getUsername())) {
                    currentVote = vote;
                    break;
                }
            }
        }

        model.addAttribute("catalog",blog.getCatalog());
        model.addAttribute("user",userService.getUserByUsername(username));
        model.addAttribute("isBlogOwner", isBlogOwner);
        model.addAttribute("blog",blog);
        model.addAttribute("currentVote",currentVote);

        return "userspace/blog";
    }

    @DeleteMapping("/{username}/blogs/{id}")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username")String username,
                                               @PathVariable("id")Long id){
        try {
            blogService.removeBlog(id);
        }catch (Exception e){
            return ResponseUtil.error(e);
        }
        String redirectUrl="/u/"+username+"/blogs";
        return ResponseUtil.success(redirectUrl);
    }

    @GetMapping("/{username}/blogs/edit")
    public String editBlog(
            @PathVariable("username") String username,Model model) {
        User user = userService.getUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);
        model.addAttribute("fileServerUrl",fileServerUrl);
        model.addAttribute("blog",new Blog());
        model.addAttribute("catalogs",catalogs);
        return "userspace/blogedit";
    }

    @GetMapping("/{username}/blogs/edit/{id}")
    public String editBlog(@PathVariable("username")String username,
                           @PathVariable("id")Long id,Model model){
        User user = userService.getUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);
        model.addAttribute("fileServerUrl",fileServerUrl);
        model.addAttribute("blog",blogService.getBlogById(id));
        model.addAttribute("catalogs",catalogs);
        return "userspace/blogedit";
    }

    @PostMapping("/{username}/blogs/edit")
    public ResponseEntity<Response> saveBlog(@PathVariable("username")String username,
                                             @RequestBody Blog blog,Model model){
        if (blog.getCatalog().getId()==null){
            return ResponseUtil.error("未选择分类");
        }
        try {
            if (blog.getId()!=null){
                Blog oldBlog = blogService.getBlogById(blog.getId());
                JPAUpdateUtil.copyNotNullProperity(blog,oldBlog);
                blogService.saveBlog(oldBlog);
            }else {
                User user = userService.getUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }
        }catch (Exception e){
            return ResponseUtil.error(e);
        }
        String redirectUrl="/u/"+username+"/blogs/"+blog.getId();
        return ResponseUtil.success(redirectUrl);
    }
}
