package com.example.leviathan.repository;

import com.example.leviathan.domain.MyProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyProblemRepository extends JpaRepository<MyProblem, Long> {

}
