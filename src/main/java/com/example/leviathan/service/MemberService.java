package com.example.leviathan.service;


import com.example.leviathan.domain.Member;
import com.example.leviathan.dto.LoginDTO;
import com.example.leviathan.dto.SaveProblemDTO;
import com.example.leviathan.dto.SignUpDTO;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;

    //토큰으로 멤버 객체를 받아옴
    //토큰으로 memberId가 필요하면 controller에서
    //public ResponseEntity<List<RentDTO>> memberRentList(HttpServletRequest header){
    //          List<RentDTO> list = itemRentService.memberRentList(jwtUtility.getStudentId(jwtUtility.resolveToken(header)));
    //위에 요런식으로 HttpServletRequest 쓰면 괜찮을듯 아님말고...        |--------여기가 토큰으로 검증부터 ID 뽑아오는 로직임------------|
    //별로면 말해용 바꾸면 되니까

    @Transactional
    public ResponseEntity<?> tokenToMember(HttpServletRequest request) {
        try {
            String token = jwtUtility.resolveToken(request);
            String memberId = jwtUtility.getMemberId(token);

            return findByMemberId(memberId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 정보 조회 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public Member tokenToMember(String token) {
        try {
            // 토큰에서 memberId 추출
            String memberId = jwtUtility.getMemberId(token);

            // findByMemberId 메소드에서 ResponseEntity<Member> 반환을 받기
            ResponseEntity<Member> response = findByMemberId(memberId);

            // ResponseEntity에서 Member 객체 꺼내기
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();  // 성공적으로 Member 객체 반환
            } else {
                // 예외 처리, 404 Not Found일 경우 적절한 처리를 할 수 있습니다
                throw new RuntimeException("해당 사용자를 찾을 수 없습니다.");
            }

        } catch (Exception e) {
            // 예외 처리 로직 (로그 남기기 등)
            throw new RuntimeException("회원 정보 조회 중 오류가 발생했습니다.", e);
        }
    }

    private ResponseEntity<Member> findByMemberId(String memberId) {
        // DB에서 Member 찾기
        Member member = memberRepository.findByMemberId(memberId);

        if (member != null) {
            return ResponseEntity.ok(member);  // Member를 OK와 함께 반환
        } else {
            // Member가 없을 경우 NOT_FOUND 상태와 함께 적절한 메시지 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);  // 실제로는 적절한 예외를 던지는 것이 좋습니다.
        }
    }


    @Transactional
    public ResponseEntity<String> signUp(SignUpDTO dto){

        try{
            // 비밀번호 암호화 필요
            // 중복 확인
            Member member = memberRepository.findByMemberId((dto.getMemberId()));
            if (member != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("이미 사용 중인 회원 ID입니다.");
            } else {
                // 새로운 회원 저장
                memberRepository.save(new Member(dto.getMemberId(), dto.getPassword()));
                return ResponseEntity.ok("회원가입 성공");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입 중 오류가 발생했습니다.");
        }
    }



    public ResponseEntity<String> login(LoginDTO dto){

        Member member = memberRepository.findByMemberId((dto.getMemberId()));
        if (member != null && member.getPassword().equals(dto.getPassword())) {
            String token = jwtUtility.generateToken(dto.getMemberId());
            return ResponseEntity.ok(token);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("로그인 오류!!");
        }
    }




    @Transactional
    public ResponseEntity<String> deleteMember(Long memberId){
        // 1. 해당 memberId로 멤버가 존재하는지 확인
        Optional<Member> member = memberRepository.findById(memberId);

        if (!member.isPresent()) {
            // 멤버가 존재하지 않으면 404 오류 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        // 2. 멤버가 존재하면 삭제 처리
        memberRepository.deleteById(memberId);
        // 3. 삭제 완료 후 성공 메시지 반환
        return ResponseEntity.ok("Member deleted successfully");
    }


}
