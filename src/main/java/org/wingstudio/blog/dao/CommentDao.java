package org.wingstudio.blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wingstudio.blog.po.Comment;

public interface CommentDao extends JpaRepository<Comment,Long> {
}
