package org.wingstudio.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wingstudio.blog.po.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User save(User user);

    User register(User user);

    void remoteUser(Long id);

    User getUserById(Long id);

    User getUserByUsername(String username);

    Page<User> listUsersByNameLike(String name, Pageable pageable);

    List<User> listUsersByUsernames(Collection<String> usernames);
}
