package com.example.leviathan.member.service;

import com.example.leviathan.member.exception.MemberErrorCode;
import com.example.leviathan.member.exception.MemberException;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.db.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findByLoginId(String loginId) {
        return memberRepository.findByMemberId(loginId);
    }

    @Transactional
    public Member register(String memberId, String encodedPassword) {
        if (memberRepository.existsByMemberId(memberId)) {
            throw new MemberException(MemberErrorCode.MEMBER_ALREADY_REGISTERED);
        }
        return memberRepository.save(new Member(memberId, encodedPassword));
    }

    @Transactional
    public boolean registerAdminIfAbsent(String memberId, String encodedPassword) {
        if (memberRepository.existsByMemberId(memberId)) {
            return false;
        }
        memberRepository.save(Member.createAdmin(memberId, encodedPassword));
        return true;
    }

    @Transactional
    public boolean delete(Long id) {
        if (memberRepository.findById(id).isEmpty()) {
            return false;
        }
        memberRepository.deleteById(id);
        return true;
    }
}
