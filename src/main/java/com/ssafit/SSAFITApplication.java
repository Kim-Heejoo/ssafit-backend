package com.ssafit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SSAFITApplication {
	public static void main(String[] args) {
		
		SpringApplication.run(SSAFITApplication.class, args);
		
	}
}
