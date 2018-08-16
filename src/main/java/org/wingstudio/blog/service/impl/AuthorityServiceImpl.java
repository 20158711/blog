package org.wingstudio.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wingstudio.blog.dao.AuthorityDao;
import org.wingstudio.blog.po.Authority;
import org.wingstudio.blog.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityDao dao;

    public Authority getAuthorityById(Long id){
        return dao.findById(id).get();
    }

}
