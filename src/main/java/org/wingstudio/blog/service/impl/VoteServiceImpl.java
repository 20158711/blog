package org.wingstudio.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wingstudio.blog.dao.VoteDao;
import org.wingstudio.blog.po.Vote;
import org.wingstudio.blog.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteDao voteDao;

    @Override
    public Vote getVoteById(Long id) {
        return voteDao.findById(id).get();
    }

    @Override
    public void removeVote(Long id) {
        voteDao.deleteById(id);
    }
}
