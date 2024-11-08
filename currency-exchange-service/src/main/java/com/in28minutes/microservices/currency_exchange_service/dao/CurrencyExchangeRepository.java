package com.in28minutes.microservices.currency_exchange_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in28minutes.microservices.currency_exchange_service.entity.CurrencyExchange;

public interface CurrencyExchangeRepository 
	extends JpaRepository<CurrencyExchange, Long> {

	public CurrencyExchange findByFromAndTo(String from, String to);
}
