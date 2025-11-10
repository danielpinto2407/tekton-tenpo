package com.tekton.tenpo;

import org.springframework.boot.SpringApplication;

public class TestTenpoApplication {

	public static void main(String[] args) {
		SpringApplication.from(TenpoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
