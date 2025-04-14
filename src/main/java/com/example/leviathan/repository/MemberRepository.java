package com.example.leviathan.repository;

import com.example.leviathan.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByMemberId(String memberId);
    Member findByMemberId(String memberId);
}
