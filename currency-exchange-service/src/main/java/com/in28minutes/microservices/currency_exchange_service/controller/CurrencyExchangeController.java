package com.in28minutes.microservices.currency_exchange_service.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.microservices.currency_exchange_service.dao.CurrencyExchangeRepository;
import com.in28minutes.microservices.currency_exchange_service.entity.CurrencyExchange;

@RestController
public class CurrencyExchangeController {	
	
	private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
	
	private Environment environment;
	
	@Autowired
	private CurrencyExchangeRepository ceRepository;
	
	public CurrencyExchangeController(Environment theEnvironment) {
		this.environment = theEnvironment;
	}

	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeValue(
			@PathVariable String from,
			@PathVariable String to) {
		
		logger.info("retrieveExchangeValue called with {} to {}", from, to);
		
		CurrencyExchange currencyExchange = ceRepository.findByFromAndTo(from, to);

		if(currencyExchange == null) {
			throw new RuntimeException("Unable to find entry for Currency Exchange: " + from + " to " + to );
		}		
		
		String serverPort = environment.getProperty("local.server.port");
		currencyExchange.setEnvironment(serverPort );		
		
		return currencyExchange;
	}
}
