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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemGenerationService {

    private final ProblemRepository problemRepository;
    private final MyProblemRepository myProblemRepository;
    private final ProblemGenerator problemGenerator;
    private final ProblemExecutor problemExecutor;

    public boolean isValidTopic(String topic) {
        return problemGenerator.isValidTopic(topic);
    }

    public GeneratedProblem preview(String topic, int level) {
        return problemGenerator.generate(topic, level);
    }

    @Transactional
    public Problem generate(Member member, String topic, int level) {
        GeneratedProblem generated = problemGenerator.generate(topic, level);

        Problem problem = Problem.fromGenerated(generated);
        problemRepository.save(problem);

        problem.assignNumberFromId(); // id를 number에 복사
        problemRepository.save(problem); // 다시 저장

        myProblemRepository.save(new MyProblem(problem, member));

        problemExecutor.prepare(problem, generated);

        return problem;
    }

    public List<Problem> findGeneratedBy(Member member) {
        return myProblemRepository.findAllByMemberId(member.getId()).stream()
                .map(MyProblem::getProblem)
                .toList();
    }
}
