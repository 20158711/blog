package org.wingstudio.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wingstudio.blog.dao.CommentDao;
import org.wingstudio.blog.po.Comment;
import org.wingstudio.blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao dao;

    @Override
    public Comment getCommentById(Long id) {
        return dao.findById(id).get();
    }

    @Override
    public void removeComment(Long id) {
        dao.deleteById(id);
    }
}
