package com.example.leviathan.service;


import com.example.leviathan.domain.Problem;
import com.example.leviathan.dto.SaveProblemDTO;
import com.example.leviathan.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository problemRepository;


    @Transactional
    public ResponseEntity<String> saveProblem(SaveProblemDTO dto) {
        // 1. 문제 번호가 이미 존재하는지 확인
        Problem existingProblem = problemRepository.findByNumber(dto.getNumber());

        // 2. 만약 해당 번호의 문제가 이미 존재하면, 에러 메시지 반환
        if (existingProblem != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem with this number already exists");
        }

        // 3. 새로운 Problem 객체 생성
        Problem problem = new Problem(dto.getNumber(), dto.getTitle(), dto.getQuestion(), dto.getHint());

        // 4. 새로운 문제 저장
        problemRepository.save(problem);

        // 5. 저장 완료 후 성공 메시지 반환
        return ResponseEntity.status(HttpStatus.CREATED).body("Problem saved successfully");
    }


}
