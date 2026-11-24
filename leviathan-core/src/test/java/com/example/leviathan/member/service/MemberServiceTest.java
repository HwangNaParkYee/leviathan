package com.example.leviathan.member.service;

import com.example.leviathan.member.exception.MemberErrorCode;
import com.example.leviathan.member.exception.MemberException;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.db.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 성공")
    void register_Success() {
        when(memberRepository.existsByMemberId("testUser")).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Member member = memberService.register("testUser", "encrypted");

        assertEquals("testUser", member.getMemberId());
        assertEquals("encrypted", member.getPassword());
        assertFalse(member.isAdmin());
    }

    @Test
    @DisplayName("이미 사용 중인 ID로는 가입할 수 없다")
    void register_Fail_Duplicate() {
        when(memberRepository.existsByMemberId("duplicateUser")).thenReturn(true);

        MemberException e = assertThrows(MemberException.class, () -> memberService.register("duplicateUser", "encrypted"));

        assertEquals(MemberErrorCode.MEMBER_ALREADY_REGISTERED, e.getErrorCode());
        verify(memberRepository, never()).save(any());
    }

    @Test
    @DisplayName("관리자 계정은 없을 때만 생성한다")
    void registerAdminIfAbsent() {
        when(memberRepository.existsByMemberId("admin")).thenReturn(false, true);

        assertTrue(memberService.registerAdminIfAbsent("admin", "encrypted"));
        assertFalse(memberService.registerAdminIfAbsent("admin", "encrypted"));
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("없는 회원은 삭제하지 않는다")
    void delete_NotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(memberService.delete(1L));
        verify(memberRepository, never()).deleteById(any());
    }
}
