package com.db.dsg.service;

import com.db.dsg.dtos.MemberOnboardingRequest;
import com.db.dsg.model.Group;
import com.db.dsg.model.Member;
import com.db.dsg.model.MemberUser;
import com.db.dsg.repository.GroupRepository;
import com.db.dsg.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    // ✅ Onboard a member into a group and link with user (if needed)
    public Member onboardMember(Long groupId, Member member) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        member.setGroup(group);
        return memberRepository.save(member);
    }

    // ✅ Get all members
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // ✅ Get all members in a group
    public List<Member> getMembersByGroup(Long groupId) {
        return memberRepository.findByGroupId(groupId);
    }

    // ✅ Get single member by ID
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    // ✅ Update member profile
    public Member updateMember(Long id, Member updated) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.setName(updated.getName());
        member.setPhone(updated.getPhone());
        member.setAadhaar(updated.getAadhaar());

        return memberRepository.save(member);
    }

    // ✅ Delete member
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    // ✅ Fetch member by logged-in user
    public Member getByUser(MemberUser user) {
        return memberRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Linked member not found"));
    }
}
