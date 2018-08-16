package org.wingstudio.blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wingstudio.blog.po.Vote;

public interface VoteDao extends JpaRepository<Vote,Long> {

}
