package com.example.leviathan.problem.db.repository;

import com.example.leviathan.problem.db.entity.MyProblem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MyProblemRepositoryImpl implements MyProblemRepository {

    private final MyProblemJpaRepository jpaRepository;

    @Override
    public MyProblem save(MyProblem myProblem) {
        return jpaRepository.save(myProblem);
    }

    @Override
    public List<MyProblem> findAllByMemberId(Long memberId) {
        return jpaRepository.findAllByMemberId(memberId);
    }
}
