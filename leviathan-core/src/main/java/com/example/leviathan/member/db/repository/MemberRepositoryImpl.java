package com.example.leviathan.member.db.repository;

import com.example.leviathan.member.db.entity.Member;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository jpaRepository;

    @Override
    public Member save(Member member) {
        return jpaRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByMemberId(String memberId) {
        return jpaRepository.existsByMemberId(memberId);
    }

    @Override
    public Member findByMemberId(String memberId) {
        return jpaRepository.findByMemberId(memberId);
    }
}
