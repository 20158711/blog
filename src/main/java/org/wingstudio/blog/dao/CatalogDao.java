package org.wingstudio.blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wingstudio.blog.po.Catalog;
import org.wingstudio.blog.po.User;

import java.util.List;

public interface CatalogDao extends JpaRepository<Catalog,Long> {
    List<Catalog> findAllByUser(User user);
    List<Catalog> findByUserAndName(User user,String name);
}
