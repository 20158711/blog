package org.wingstudio.blog.service;

import org.wingstudio.blog.po.Comment;

public interface CommentService {

    Comment getCommentById(Long id);

    void removeComment(Long id);

}
