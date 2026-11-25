package com.example.leviathan.problem.facade;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.problem.model.GeneratedProblem;
import com.example.leviathan.problem.controller.request.UserPromptRequest;
import com.example.leviathan.problem.service.ProblemGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromptFacade {

    private final ProblemGenerationService problemGenerationService;
    private final MemberService memberService;

    // 리눅스 실습 문제로 적절한 요청인지 확인
    public boolean isValidPrompt(String prompt) {
        return problemGenerationService.isValidTopic(prompt);
    }

    // 저장 없이 문제 생성 결과만 반환
    public GeneratedProblem generateProblem(UserPromptRequest dto) {
        return problemGenerationService.preview(dto.prompt(), dto.level());
    }

    // 문제를 생성해서 저장하고 문제 id 반환
    @Transactional
    public Long savePromptProblem(UserPromptRequest dto, String loginId) {
        Member member = memberService.findByLoginId(loginId);

        return problemGenerationService.generate(member, dto.prompt(), dto.level()).getId();
    }
}
