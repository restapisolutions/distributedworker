package com.interview.assignment.distributedworker;

import com.interview.assignment.distributedworker.service.HttpService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
import java.util.Map;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
class DistributedworkerApplicationTests {

	@Test
	public void fetchWebsiteData(){
	//NOTE : turn on the @Profile annotation inside the httpService class to run this test. Profiles are used because I'm running business logic on startup but I don't need it for the test.
	HttpService httpService = new HttpService();
	Map<String,String> result = httpService.fetch("https://proxify.io");

	//I assume the website is UP so it should return 200
	assertEquals(result.get("statusCode"),"200");
	}

}
