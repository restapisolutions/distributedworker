package com.interview.assignment.distributedworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class DistributedworkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedworkerApplication.class, args);
	}

}
