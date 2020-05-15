package com.gdu.cashbook;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
//	 properties									 @에노테이션
// @Configuration + @EnableAutoConfiguration + @ComponentScan
@PropertySource("classpath:google.properties")	// source는 google.properties 에서
public class CashbookApplication {
	@Value("${google.username}")				// 파일안에 google.username이라는 변수의 값을 가져온다.
	public String username;						// @Value() 는 기본타입에 값을 넣는것..
	@Value("${google.password}")
	public String password;
	
	
	public static void main(String[] args) {
		SpringApplication.run(CashbookApplication.class, args);
	}
	
	// Bean을 만드는 에노테이션
	// MailSender 인터페이스를 상속받음.. 다형성
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("smtp.gmail.com"); // 메일 서버이름
		javaMailSender.setPort(587);	// 포트
		System.out.println(username);	// 디버깅
		System.out.println(password);	// 디버깅
		javaMailSender.setUsername(username);	// 아이디
		javaMailSender.setPassword(password);	// 비밀번호
		
		Properties prop = new Properties();	// ProPerties == HashMap<String, String>
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		
		javaMailSender.setJavaMailProperties(prop);
		
		return javaMailSender;
	}
}
