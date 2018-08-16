package org.wingstudio.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.wingstudio.blog.common.Response;
import org.wingstudio.blog.util.SecurityUtil;
import org.wingstudio.blog.po.Blog;
import org.wingstudio.blog.po.Comment;
import org.wingstudio.blog.po.User;
import org.wingstudio.blog.service.BlogService;
import org.wingstudio.blog.service.CommentService;
import org.wingstudio.blog.util.ResponseUtil;

import java.util.List;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String listComments(@RequestParam("blogId")Long blogId,Model model){
        Blog blog = blogService.getBlogById(blogId);
        List<Comment> comments = blog.getComments();
        String commentOwner="";
        User user=SecurityUtil.getUser();
        if (user != null) {
            commentOwner=user.getUsername();
        }
        model.addAttribute("commentOwner",commentOwner);
        model.addAttribute("comments",comments);
        return "/userspace/blog::#mainContainerRepleace";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createComment(@RequestParam("blogId") Long blogId,
                                                  @RequestParam("commentContent") String commentContent){
        try {
            blogService.createComment(blogId,commentContent);
        }catch (Exception e){
            return ResponseUtil.error(e);
        }
        return ResponseUtil.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> delete(@PathVariable("id")Long id,Long blogId){
        User user=commentService.getCommentById(id).getUser();
        boolean isOwner=SecurityUtil.isOwner(user.getUsername());
        if (!isOwner){
            return ResponseUtil.error("没有操作权限");
        }
        try {
            blogService.removeComment(blogId,id);
            commentService.removeComment(id);
        }catch (Exception e){
            return ResponseUtil.error(e);
        }
        return ResponseUtil.success();

    }

}
