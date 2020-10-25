package com.interview.assignment.distributedworker;

import com.interview.assignment.distributedworker.service.HttpService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
class DistributedworkerApplicationTests {

	@Test
	public void fetchWebsiteData(){
	HttpService httpService = new HttpService();
	Map<String,String> result = httpService.fetch("https://proxify.io");

	//I assume the website is UP so it should return 200
	assertEquals("200",result.get("http_code"));
	}

}
