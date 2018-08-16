package org.wingstudio.blog.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wingstudio.blog.po.User;

import java.util.Collection;
import java.util.List;

public interface UserDao extends JpaRepository<User,Long> {

    Page<User> findAllByNameLike(String name, Pageable pageable);

    User findByUsername(String username);

    List<User> findByUsernameIn(Collection<String> usernames);


}
