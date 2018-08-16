package org.wingstudio.blog.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wingstudio.blog.po.Blog;
import org.wingstudio.blog.po.Catalog;
import org.wingstudio.blog.po.User;

public interface BlogDao extends JpaRepository<Blog,Long> {
    Page<Blog> findAllByCatalog(Catalog catalog, Pageable pageable);
    Page<Blog> findAllByUser(User user,Pageable pageable);
    Page<Blog> findAllByUserAndTitleLikeOrderByCreateTime(User user,String title,Pageable pageable);
    Page<Blog> findAllByUserAndTitleLike(User user,String title,Pageable pageable);
    Page<Blog> findAllByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTime(String title,User user,String tags,User user2,Pageable pageable);

}
