package com.example.leviathan.member.db.repository;

import com.example.leviathan.member.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    boolean existsByMemberId(String memberId);
    Member findByMemberId(String memberId);
}
