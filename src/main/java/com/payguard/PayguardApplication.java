package com.payguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class PayguardApplication {

	public static void main(String[] args) {
		// .env 파일에서 환경 변수 로드
        Dotenv dotenv = Dotenv.load();
        
        // Spring Boot가 사용할 수 있도록 시스템 프로퍼티로 설정
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        
		SpringApplication.run(PayguardApplication.class, args);
	}

}
