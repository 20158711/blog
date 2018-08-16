package org.wingstudio.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.wingstudio.blog.common.Const;
import org.wingstudio.blog.service.EsBlogService;
import org.wingstudio.blog.util.SecurityUtil;
import org.wingstudio.blog.dao.BlogDao;
import org.wingstudio.blog.po.*;
import org.wingstudio.blog.service.BlogService;
import org.wingstudio.blog.util.StringUtil;

import javax.transaction.Transactional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private EsBlogService esBlogService;

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        return blogDao.findAllByCatalog(catalog, pageable);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogDao.findById(id).get();
    }

    @Override
    public void removeBlog(Long id) {
        blogDao.deleteById(id);
        EsBlog esBlog=esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esBlog.getId());
    }

    @Override
    @Transactional
    public Blog saveBlog(Blog blog) {
        boolean isNew=blog.getId()==null;
        Blog returnBlog=blogDao.save(blog);
        EsBlog esBlog=null;

        if (isNew) {
            esBlog = new EsBlog(returnBlog);
        } else{
            esBlog=esBlogService.getEsBlogByBlogId(blog.getId());
            esBlog.update(returnBlog);
        }
        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    @Override
    public Page<Blog> listBlogsByOrder(User user, String order, Long catalogId, String keyword, int pageIndex, int pageSize) {
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        if (catalogId != null && catalogId > 0) {
            return blogDao.findAllByCatalog(new Catalog(catalogId), pageable);
        } else if (Const.ORDER_PROPERITY.HOT.equals(order)) {
            Sort sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "voteSize");
            pageable = new PageRequest(pageIndex, pageSize, sort);
        }
        return listBlogs(user, keyword, pageable);
    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Blog blog = blogDao.findById(blogId).get();
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment=new Comment(user,commentContent);
        blog.addComment(comment);
        return saveBlog(blog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog blog = blogDao.findById(blogId).get();
        blog.removeComment(commentId);
        saveBlog(blog);
    }

    @Override
    public Blog createVote(Long blogId) {
        Blog oldBlog = blogDao.findById(blogId).get();
        User user=SecurityUtil.getUser();
        Vote vote=new Vote(user);
        boolean isExist=oldBlog.addVote(vote);
        if (isExist)
            throw new IllegalArgumentException("你已经点过赞了");
        return this.saveBlog(oldBlog);
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
        Blog blog = blogDao.findById(blogId).get();
        blog.removeVote(voteId);
        this.saveBlog(blog);
    }

    public void readingIncrease(Long id) {
        Blog blog = blogDao.findById(id).get();
        blog.setReadSize(blog.getReadSize()+1);
        this.saveBlog(blog);
    }

    private Page<Blog> listBlogs(User user, String title, Pageable pageable) {
        Page<Blog> page;
        if (StringUtil.isBlank(title)) {
            page = blogDao.findAllByUser(user, pageable);
        } else {
            title = "%" + title + "%";
            page = blogDao.findAllByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTime(title, user, title, user, pageable);
        }
        return page;
    }
}
