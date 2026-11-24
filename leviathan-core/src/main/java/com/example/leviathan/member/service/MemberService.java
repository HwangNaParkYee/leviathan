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

    // 로그인 ID(memberId)로 회원 조회. 없으면 null
    public Member findByLoginId(String loginId) {
        return memberRepository.findByMemberId(loginId);
    }

    // 회원 가입 (password는 암호화된 값)
    @Transactional
    public Member register(String memberId, String encodedPassword) {
        if (memberRepository.existsByMemberId(memberId)) {
            throw new MemberException(MemberErrorCode.MEMBER_ALREADY_REGISTERED);
        }
        return memberRepository.save(new Member(memberId, encodedPassword));
    }

    // 관리자 계정이 없으면 생성. 생성했으면 true
    @Transactional
    public boolean registerAdminIfAbsent(String memberId, String encodedPassword) {
        if (memberRepository.existsByMemberId(memberId)) {
            return false;
        }
        memberRepository.save(Member.createAdmin(memberId, encodedPassword));
        return true;
    }

    // 회원 삭제. 회원이 없으면 false
    @Transactional
    public boolean delete(Long id) {
        if (memberRepository.findById(id).isEmpty()) {
            return false;
        }
        memberRepository.deleteById(id);
        return true;
    }
}
