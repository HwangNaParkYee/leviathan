package com.example.leviathan.problem.service;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.model.GeneratedProblem;
import com.example.leviathan.problem.db.entity.MyProblem;
import com.example.leviathan.problem.db.repository.MyProblemRepository;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.port.ProblemExecutor;
import com.example.leviathan.problem.port.ProblemGenerator;
import com.example.leviathan.problem.db.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// AI 문제 생성
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemGenerationService {

    private final ProblemRepository problemRepository;
    private final MyProblemRepository myProblemRepository;
    private final ProblemGenerator problemGenerator;
    private final ProblemExecutor problemExecutor;

    // 리눅스 실습 문제로 적절한 요청인지 확인
    public boolean isValidTopic(String topic) {
        return problemGenerator.isValidTopic(topic);
    }

    // 저장 없이 문제 생성 결과만 반환
    public GeneratedProblem preview(String topic, int level) {
        return problemGenerator.generate(topic, level);
    }

    // 문제를 생성해서 저장하고 실행 환경까지 준비
    @Transactional
    public Problem generate(Member member, String topic, int level) {
        GeneratedProblem generated = problemGenerator.generate(topic, level);

        Problem problem = Problem.fromGenerated(generated);
        problemRepository.save(problem);

        problem.assignNumberFromId(); // id를 number에 복사
        problemRepository.save(problem); // 다시 저장

        myProblemRepository.save(new MyProblem(problem, member));

        // 문제 실행 환경(Docker 이미지) 준비
        problemExecutor.prepare(problem, generated);

        return problem;
    }

    // 회원이 생성한 문제들
    public List<Problem> findGeneratedBy(Member member) {
        return myProblemRepository.findAllByMemberId(member.getId()).stream()
                .map(MyProblem::getProblem)
                .toList();
    }
}
