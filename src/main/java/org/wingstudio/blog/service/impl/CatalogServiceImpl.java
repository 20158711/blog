package org.wingstudio.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wingstudio.blog.dao.CatalogDao;
import org.wingstudio.blog.po.Catalog;
import org.wingstudio.blog.po.User;
import org.wingstudio.blog.service.CatalogService;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogDao dao;

    @Override
    public Catalog getCatalogById(Long catalogId) {
        Catalog catalog = dao.findById(catalogId).get();
        return catalog;
    }

    @Override
    public List<Catalog> listCatalogs(User user) {
        return dao.findAllByUser(user);
    }

    @Override
    public void saveCatalog(Catalog catalog) {
        List<Catalog> list = dao.findByUserAndName(catalog.getUser(), catalog.getName());
        if (!CollectionUtils.isEmpty(list)){
            throw new IllegalArgumentException("该分类已经存在");
        }
        dao.save(catalog);
    }

    @Override
    public void removeCatalog(Long id) {
        dao.deleteById(id);
    }
}
