package com.internship.accessgovernancemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
public class AccessGovernanceManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessGovernanceManagerApplication.class, args);
	}

}
