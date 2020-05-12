package com.gdu.cashbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//	 properties									 @에노테이션
// @Configuration + @EnableAutoConfiguration + @ComponentScan
public class CashbookApplication {
	public static void main(String[] args) {
		SpringApplication.run(CashbookApplication.class, args);
	}
}
