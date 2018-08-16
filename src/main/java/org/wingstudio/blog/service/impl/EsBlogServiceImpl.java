package org.wingstudio.blog.service.impl;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.wingstudio.blog.dao.EsBlogDao;
import org.wingstudio.blog.po.EsBlog;
import org.wingstudio.blog.po.User;
import org.wingstudio.blog.service.EsBlogService;
import org.wingstudio.blog.service.UserService;
import org.wingstudio.blog.vo.TagVo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

@Service
public class EsBlogServiceImpl implements EsBlogService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private EsBlogDao esBlogDao;

    @Autowired
    private UserService userService;

    private static final Pageable TOP5_PAGEABLE = new PageRequest(0, 5);
    private static final String EMPTY = "";

    @Override
    public void removeEsBlog(String id) {
        esBlogDao.deleteById(id);
    }

    @Override
    public EsBlog updateEsBlog(EsBlog esBlog) {
        return esBlogDao.save(esBlog);
    }

    @Override
    public EsBlog getEsBlogByBlogId(Long blogId) {
        return esBlogDao.findByBlogId(blogId);
    }

    @Override
    public Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable) {
        Page<EsBlog> page = null;
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        pageable = updateSort(pageable, sort);
        if (keyword != null)
            page = esBlogDao.findDistinctByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);
        else
            page = esBlogDao.findAll(pageable);
        return page;
    }

    private Pageable updateSort(Pageable pageable, Sort sort) {
        if (pageable.getSort() == null)
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return pageable;
    }

    @Override
    public Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "voteSize", "createTime");
        pageable = updateSort(pageable, sort);
        if (keyword != null)
            return esBlogDao.findDistinctByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);
        else
            return esBlogDao.findAll(pageable);
    }

    @Override
    public Page<EsBlog> listEsBlogs(Pageable pageable) {
        return esBlogDao.findAll(pageable);
    }

    @Override
    public List<EsBlog> listTop5NewestEsBlogs() {
        Page<EsBlog> page = this.listNewestEsBlogs(EMPTY, TOP5_PAGEABLE);
        return page.getContent();
    }

    @Override
    public List<EsBlog> listTop5HotestEsBlogs() {
        Page<EsBlog> page = this.listHotestEsBlogs(EMPTY, TOP5_PAGEABLE);
        return page.getContent();
    }

    @Override
    public List<TagVo> listTop30Tags() {
        List<TagVo> list = new ArrayList<>();
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .withIndices("blog").withTypes("blog")
                .addAggregation(terms("tags").field("tags")
                        .order(Terms.Order.count(false)).size(30))
                .build();
        Aggregations aggregations = elasticsearchTemplate
                .query(searchQuery, SearchResponse::getAggregations);
        StringTerms modelTerms = (StringTerms) aggregations.asMap().get("tags");
        modelTerms.getBuckets().forEach(action -> list.add(new TagVo(action)));
        return list;
    }

    @Override
    public List<User> listTop10Users() {
        List<String> usernameList = new ArrayList<>();
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .withIndices("blog").withTypes("blog")
                .addAggregation(terms("users").field("username")
                        .order(Terms.Order.count(false)).size(12))
                .build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        StringTerms modelTerms = (StringTerms) aggregations.asMap().get("users");
        modelTerms.getBuckets().forEach(e -> usernameList.add(e.getKey().toString()));
        List<User> users = userService.listUsersByUsernames(usernameList);
        return users;
    }
}
