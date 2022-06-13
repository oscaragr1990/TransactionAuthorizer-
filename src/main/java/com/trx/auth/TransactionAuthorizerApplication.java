package com.trx.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ComponentScan("com.trx.auth.*")
public class TransactionAuthorizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionAuthorizerApplication.class, args);
	}

}
