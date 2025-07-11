package com.db.dsg.repository;

import com.db.dsg.model.Member;
import com.db.dsg.model.MemberUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByGroup_Id(Long groupId);
    Optional<Member> findByUser(MemberUser user);
    List<Member> findByGroupId(Long groupId);
}
