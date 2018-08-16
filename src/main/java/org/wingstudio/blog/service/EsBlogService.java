package org.wingstudio.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wingstudio.blog.po.EsBlog;
import org.wingstudio.blog.po.User;
import org.wingstudio.blog.vo.TagVo;

import java.util.List;

public interface EsBlogService {

    void removeEsBlog(String id);

    EsBlog updateEsBlog(EsBlog esBlog);

    EsBlog getEsBlogByBlogId(Long blogId);

    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);

    Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable);

    Page<EsBlog> listEsBlogs(Pageable pageable);

    List<EsBlog> listTop5NewestEsBlogs();

    List<EsBlog> listTop5HotestEsBlogs();

    List<TagVo> listTop30Tags();

    List<User> listTop10Users();

}
