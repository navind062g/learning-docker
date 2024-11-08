package com.in28minutes.microservices.currency_exchange_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {
	
	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

	@GetMapping("/sample-api")
	//@Retry(name = "sample-api", fallbackMethod = "hardcodedResponse")
    //@CircuitBreaker(name = "sample-api", fallbackMethod = "hardcodedResponse")
	@RateLimiter(name = "default")
    //@Bulkhead(name = "sample-api") 
	public String sampleApi() {
		
		/*
		 * logger.info("Sample API Call Received");
		 * 
		 * ResponseEntity<String> stringEntity = new
		 * RestTemplate().getForEntity("http://localhost:8080/dummy-api", String.class);
		 * 
		 * 
		 * return stringEntity.getBody();
		 */
		return "Sample Api";
	}
	
	public String hardcodedResponse(Exception ex) {
		return "Fallback Response";
	}
}
