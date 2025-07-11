package com.db.dsg.repository;

import com.db.dsg.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByPollIdAndMemberId(Long pollId, Long memberId);
    List<Vote> findByPollId(Long pollId);
}
