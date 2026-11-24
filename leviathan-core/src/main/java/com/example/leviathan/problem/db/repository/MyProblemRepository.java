package com.example.leviathan.problem.db.repository;

import com.example.leviathan.problem.db.entity.MyProblem;

import java.util.List;

public interface MyProblemRepository {

    MyProblem save(MyProblem myProblem);

    List<MyProblem> findAllByMemberId(Long memberId);
}
