package org.wingstudio.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wingstudio.blog.common.Const;
import org.wingstudio.blog.common.Response;
import org.wingstudio.blog.po.User;
import org.wingstudio.blog.service.BlogService;
import org.wingstudio.blog.service.VoteService;
import org.wingstudio.blog.util.ResponseUtil;
import org.wingstudio.blog.util.SecurityUtil;

@Controller
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createVote(Long blogId){
        try {
            blogService.createVote(blogId);
        }catch (Exception e){
            return ResponseUtil.error(e);
        }
        return ResponseUtil.success("点赞成功",null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> delete(@PathVariable("id")Long id,
                                           Long blogId){
        User user = voteService.getVoteById(id).getUser();
        boolean isOwner=SecurityUtil.isOwner(user.getUsername());
        if(!isOwner)
            return ResponseUtil.error("没有操作权限");
        try {
            blogService.removeVote(blogId,id);
            voteService.removeVote(id);
        }catch (Exception e){
            return ResponseUtil.error(e);
        }
        return ResponseUtil.success("取消成功",null);
    }

}
