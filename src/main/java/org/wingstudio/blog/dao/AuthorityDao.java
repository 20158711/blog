package org.wingstudio.blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wingstudio.blog.po.Authority;

public interface AuthorityDao extends JpaRepository<Authority,Long> {
}
