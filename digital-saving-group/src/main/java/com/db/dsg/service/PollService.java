package com.db.dsg.service;

import com.db.dsg.model.*;
import com.db.dsg.repository.MemberRepository;
import com.db.dsg.repository.PollRepository;
import com.db.dsg.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PollService {
    private final PollRepository pollRepo;
    private final VoteRepository voteRepo;
    private final MemberRepository memberRepo;

    public Poll createPoll(Poll poll) {
        return pollRepo.save(poll);
    }

    public Vote vote(Long pollId, MemberUser user, VoteOption choice) {
        Poll poll = pollRepo.findById(pollId).orElseThrow();
        Member member = memberRepo.findByUser(user).orElseThrow();

        if (poll.isClosed()) throw new RuntimeException("Poll is closed");

        voteRepo.findByPollIdAndMemberId(pollId, member.getId())
                .ifPresent(v -> { throw new RuntimeException("Already voted"); });

        Vote vote = new Vote(null, poll, member, choice, LocalDateTime.now());
        return voteRepo.save(vote);
    }

    public Map<VoteOption, Long> tallyVotes(Long pollId) {
        List<Vote> votes = voteRepo.findByPollId(pollId);
        return votes.stream()
                .collect(Collectors.groupingBy(Vote::getChoice, Collectors.counting()));
    }

    public void closePoll(Long pollId, MemberUser user) {
        Member member = memberRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User not linked"));

        if (!member.getRole().equals(Role.PRESIDENT)) {
            throw new AccessDeniedException("Only PRESIDENT can close poll");
        }

        Poll poll = pollRepo.findById(pollId).orElseThrow();
        poll.setClosed(true);
        pollRepo.save(poll);
    }
}
