package com.finance.finance_bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FinanceBridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceBridgeApplication.class, args);
	}

}
