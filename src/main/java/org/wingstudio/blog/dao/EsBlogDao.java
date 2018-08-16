package org.wingstudio.blog.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.wingstudio.blog.po.EsBlog;

public interface EsBlogDao extends ElasticsearchRepository<EsBlog,String> {

    Page<EsBlog> findDistinctByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title, String summary, String content, String tags, Pageable pageable);

    EsBlog findByBlogId(Long blogId);

}
