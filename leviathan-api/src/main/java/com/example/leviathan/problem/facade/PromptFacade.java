package com.example.leviathan.problem.facade;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.problem.exception.ProblemErrorCode;
import com.example.leviathan.problem.exception.ProblemException;
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

    public GeneratedProblem generateProblem(UserPromptRequest dto) {
        return problemGenerationService.preview(dto.prompt(), dto.level());
    }

    @Transactional
    public Long savePromptProblem(UserPromptRequest dto, String loginId) {
        if (!problemGenerationService.isValidTopic(dto.prompt())) {
            throw new ProblemException(ProblemErrorCode.INVALID_PROMPT);
        }

        Member member = memberService.findByLoginId(loginId);

        return problemGenerationService.generate(member, dto.prompt(), dto.level()).getId();
    }
}
