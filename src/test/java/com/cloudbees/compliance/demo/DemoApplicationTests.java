package com.cloudbees.compliance.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

//	@Test
//	public void contextLoads() {
//	}

	@Test
	public void circumference() {
		assert DemoApplication.circumference(1) > 0;
	}
}
