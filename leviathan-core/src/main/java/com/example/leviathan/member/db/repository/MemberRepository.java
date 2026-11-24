package com.example.leviathan.member.db.repository;

import com.example.leviathan.member.db.entity.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long id);

    void deleteById(Long id);

    boolean existsByMemberId(String memberId);

    Member findByMemberId(String memberId);
}
