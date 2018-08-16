package org.wingstudio.blog.service;

import org.wingstudio.blog.po.Catalog;
import org.wingstudio.blog.po.User;

import java.util.List;

public interface CatalogService {

    Catalog getCatalogById(Long catalogId);

    List<Catalog> listCatalogs(User user);

    void saveCatalog(Catalog catalog);

    void removeCatalog(Long id);
}
