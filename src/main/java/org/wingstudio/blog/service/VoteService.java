package org.wingstudio.blog.service;

import org.wingstudio.blog.po.Vote;

public interface VoteService {

    Vote getVoteById(Long id);

    void removeVote(Long id);

}
