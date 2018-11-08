package com.cloudbees.compliance.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("Test pr");
		SpringApplication.run(DemoApplication.class, args);
	}

	public Boolean booleanMethod() {
		return null;
	}

	public static double circumference(double diameter) {
		return diameter * 3.141;
	}

	@Override
	public boolean equals(Object obj) {
		DemoApplication toCompare = (DemoApplication) obj;
		return toCompare == this;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
