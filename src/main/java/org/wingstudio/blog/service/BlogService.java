package org.wingstudio.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wingstudio.blog.po.Blog;
import org.wingstudio.blog.po.Catalog;
import org.wingstudio.blog.po.User;


public interface BlogService {

    Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable);

    Blog getBlogById(Long id);

    void removeBlog(Long id);

    void readingIncrease(Long id);

    Blog saveBlog(Blog oldBlog);

    Page<Blog> listBlogsByOrder(User user,String order, Long catalogId, String keyword, int pageIndex, int pageSize);

    Blog createComment(Long blogId,String commentContent);

    void removeComment(Long blogId,Long commentId);

    Blog createVote(Long blogId);

    void removeVote(Long blogId,Long voteId);
}
