package org.wingstudio.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wingstudio.blog.dao.UserDao;
import org.wingstudio.blog.po.User;
import org.wingstudio.blog.service.UserService;
import org.wingstudio.blog.util.StringUtil;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService,UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    @Transactional
    public User register(User user) {
        return userDao.save(user);
    }

    @Override
    @Transactional
    public void remoteUser(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id).get();
    }

    @Override
    public Page<User> listUsersByNameLike(String name, Pageable pageable) {
        if (StringUtil.isBlank(name))
            return userDao.findAll(pageable);
        return userDao.findAllByNameLike("%"+name+"%",pageable);
    }

    @Override
    public List<User> listUsersByUsernames(Collection<String> usernames) {
        return userDao.findByUsernameIn(usernames);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByUsername(s);
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        User user=userDao.findByUsername(username);
        return user;
    }
}
