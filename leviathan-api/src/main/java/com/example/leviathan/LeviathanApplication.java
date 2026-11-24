package com.example.leviathan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

// 로그인은 MemberService에서 직접 처리하므로 Spring Security의 기본 계정 생성은 사용하지 않는다
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class LeviathanApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeviathanApplication.class, args);
	}

}
