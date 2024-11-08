package com.in28minutes.microservices.currency_conversion_service.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.in28minutes.microservices.currency_conversion_service.entity.CurrencyConversion;
import com.in28minutes.microservices.currency_conversion_service.proxy.CurrencyExchangeProxy;

@RestController
public class ConversionController {
	
	

	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion convertCurrency(
			@PathVariable String from,
			@PathVariable String to,
			@PathVariable Long quantity) {
		
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);		
		
		ResponseEntity<CurrencyConversion> resEntity =
				restTemplate
		.getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
				CurrencyConversion.class, uriVariables);
		
		CurrencyConversion conversionResponse = resEntity.getBody();
		
		CurrencyConversion currencyConversion = 
				new CurrencyConversion(conversionResponse.getId(), from, to,
						BigDecimal.valueOf(quantity),
						conversionResponse.getConversionMultiple(),
						conversionResponse.getEnvironment() + " rest template");
		
		//setting default values for now
		currencyConversion.calculateTotalCalculatedAmount();
		return currencyConversion; 
	}
	
	@Autowired
	private CurrencyExchangeProxy currencyExchangeService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/feign-currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion convertCurrencyFeign(
			@PathVariable String from,
			@PathVariable String to,
			@PathVariable Long quantity) {
		
				
		CurrencyConversion conversionResponse = currencyExchangeService.retrieveExchangeValue(from, to);
		
		CurrencyConversion currencyConversion = 
				new CurrencyConversion(conversionResponse.getId(), from, to,
						BigDecimal.valueOf(quantity),
						conversionResponse.getConversionMultiple(),
						conversionResponse.getEnvironment() + " feign");
		
		//setting default values for now
		currencyConversion.calculateTotalCalculatedAmount();
		return currencyConversion; 
	}
}

@Configuration(proxyBeanMethods = false)
class RestTemplateConfiguration {
    
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
